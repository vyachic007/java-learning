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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @KafkaListener(topics = TOPIC, batch = "true", containerFactory = "batchKafkaListenerContainerFactory")
    public void listen(List<String> messages) {
        log.info("Получена пачка из {} сообщений", messages.size());

        for (String messageJson : messages) {
            try {
                TransferMessage message = objectMapper.readValue(messageJson, TransferMessage.class);
                log.info("Начало обработки сообщения: id={}, со счета={}, на счет={}, сумма={}",
                        message.getId(),
                        message.getFromAccountId(),
                        message.getToAccountId(),
                        message.getAmount());

                processMessage(message);

                log.info("Успешная обработка сообщения: id={}", message.getId());

            } catch (Exception e) {
                log.error("Ошибка при обработке сообщения: {}", messageJson, e);
                throw new RuntimeException("Ошибка обработки сообщения", e);
            }
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void processMessage(TransferMessage message) {
        if (transferDao.isExistsById(message.getId())) {
            log.info("Перевод с id={} уже обработан, пропускаем", message.getId());
            return;
        }

        Optional<Account> fromAccountOpt = accountDao.findById(message.getFromAccountId());
        Optional<Account> toAccountOpt = accountDao.findById(message.getToAccountId());

        if (fromAccountOpt.isEmpty()) {
            log.error("Ошибка валидации: счет списания {} не найден", message.getFromAccountId());
            return;
        }

        if (toAccountOpt.isEmpty()) {
            log.error("Ошибка валидации: счет зачисления {} не найден", message.getToAccountId());
            return;
        }

        Account fromAccount = fromAccountOpt.get();
        Account toAccount = toAccountOpt.get();

        if (fromAccount.getBalance().compareTo(message.getAmount()) < 0) {
            log.error("Ошибка валидации: недостаточно средств на счете {}. Баланс={}, сумма списания={}",
                    message.getFromAccountId(),
                    fromAccount.getBalance(),
                    message.getAmount());
            return;
        }

        try {
            BigDecimal newFromBalance = fromAccount.getBalance().subtract(message.getAmount());
            BigDecimal newToBalance = toAccount.getBalance().add(message.getAmount());

            accountDao.updateBalance(message.getFromAccountId(), newFromBalance);
            accountDao.updateBalance(message.getToAccountId(), newToBalance);

            Transfer transfer = new Transfer(
                    message.getId(),
                    message.getFromAccountId(),
                    message.getToAccountId(),
                    message.getAmount(),
                    "READY"
            );
            transferDao.save(transfer);

        } catch (Exception e) {
            log.error("Ошибка выполнения транзакции перевода: id={}", message.getId(), e);

            transactionTemplate.execute(status -> {
                Transfer errorTransfer = new Transfer(
                        message.getId(),
                        message.getFromAccountId(),
                        message.getToAccountId(),
                        message.getAmount(),
                        "ERROR"
                );
                transferDao.save(errorTransfer);
                return null;
            });

            throw e;
        }
    }
}