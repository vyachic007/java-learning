package by.slava_borisov.consumer.service;

import by.slava_borisov.consumer.dao.AccountDao;
import by.slava_borisov.consumer.dao.TransferDao;
import by.slava_borisov.consumer.model.Account;
import by.slava_borisov.consumer.model.Transfer;
import by.slava_borisov.consumer.model.TransferMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferConsumerService {

    private final AccountDao accountDao;
    private final TransferDao transferDao;
    private final ObjectMapper objectMapper;
    private final TransactionTemplate transactionTemplate;

    private static final String TOPIC = "transfers";

    @KafkaListener(topics = TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void listen(List<String> messages, Acknowledgment ack) {

        log.info("Получена пачка сообщений, размер={}", messages.size());

        for (String messageJson : messages) {
            try {
                log.info("Начало обработки сообщения");

                TransferMessage message = objectMapper.readValue(messageJson, TransferMessage.class);
                processMessage(message);

            } catch (Exception e) {
                log.error("Ошибка обработки сообщения: {}", messageJson, e);
            }
        }

        ack.acknowledge();
    }

    public void processMessage(TransferMessage message) {

        if (transferDao.isExistsById(message.getId())) {
            log.info("Перевод с id={} уже обработан, пропускаем", message.getId());
            return;
        }

        Optional<Account> fromAccountOpt = accountDao.findById(message.getFromAccountId());
        Optional<Account> toAccountOpt = accountDao.findById(message.getToAccountId());

        if (fromAccountOpt.isEmpty()) {
            log.error("Ошибка валидации: счет списания {} не найден", message.getFromAccountId());
            saveErrorTransfer(message);
            return;
        }

        if (toAccountOpt.isEmpty()) {
            log.error("Ошибка валидации: счет зачисления {} не найден", message.getToAccountId());
            saveErrorTransfer(message);
            return;
        }

        Account fromAccount = fromAccountOpt.get();
        Account toAccount = toAccountOpt.get();

        if (fromAccount.getBalance().compareTo(message.getAmount()) < 0) {
            log.error("Ошибка валидации: недостаточно средств на счете {}. Баланс={}, сумма списания={}",
                    message.getFromAccountId(), fromAccount.getBalance(), message.getAmount());
            saveErrorTransfer(message);
            return;
        }

        try {
            BigDecimal newFromBalance = fromAccount.getBalance().subtract(message.getAmount());
            BigDecimal newToBalance = toAccount.getBalance().add(message.getAmount());

            transactionTemplate.execute(status -> {
                accountDao.updateBalance(message.getFromAccountId(), newFromBalance);
                accountDao.updateBalance(message.getToAccountId(), newToBalance);

                Transfer transfer = new Transfer(
                        message.getId(),
                        message.getFromAccountId(),
                        message.getToAccountId(),
                        message.getAmount(),
                        "готово"
                );

                transferDao.save(transfer);
                return null;
            });

            log.info("Успешно обработан перевод id={}", message.getId());

        } catch (Exception e) {
            log.error("Сбой при обновлении балансов для перевода id={}", message.getId(), e);

            transactionTemplate.execute(status -> {
                Transfer errorTransfer = new Transfer(
                        message.getId(),
                        message.getFromAccountId(),
                        message.getToAccountId(),
                        message.getAmount(),
                        "завершилось с ошибкой"
                );

                transferDao.save(errorTransfer);
                return null;
            });

            throw new RuntimeException("Ошибка обновления балансов", e);
        }
    }

    private void saveErrorTransfer(TransferMessage message) {
        transactionTemplate.execute(status -> {
            Transfer errorTransfer = new Transfer(
                    message.getId(),
                    message.getFromAccountId(),
                    message.getToAccountId(),
                    message.getAmount(),
                    "завершилось с ошибкой"
            );

            transferDao.save(errorTransfer);
            return null;
        });
    }
}