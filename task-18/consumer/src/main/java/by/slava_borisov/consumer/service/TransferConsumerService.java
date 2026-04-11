package by.slava_borisov.consumer.service;

import by.slava_borisov.consumer.dao.AccountDao;
import by.slava_borisov.consumer.dao.TransferDao;
import by.slava_borisov.consumer.model.Account;
import by.slava_borisov.consumer.model.Transfer;
import by.slava_borisov.consumer.model.TransferMessage;
import by.slava_borisov.consumer.model.TransferStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferConsumerService {

    private static final String TOPIC = "transfers";

    private final AccountDao accountDao;
    private final TransferDao transferDao;
    private final ObjectMapper objectMapper;
    private final TransactionTemplate transactionTemplate;

    @KafkaListener(topics = TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void listen(List<String> messages, Acknowledgment ack) {
        log.info("Получена пачка сообщений, размер={}", messages.size());

        boolean allProcessed = true;

        for (String messageJson : messages) {
            try {
                TransferMessage message = objectMapper.readValue(messageJson, TransferMessage.class);
                log.info("Начало обработки сообщения id={}", message.getId());
                processMessage(message);
            } catch (Exception e) {
                allProcessed = false;
                log.error("Ошибка обработки сообщения: {}", messageJson, e);
                break;
            }
        }

        if (allProcessed) {
            ack.acknowledge();
            log.info("Пачка сообщений успешно подтверждена");
        } else {
            throw new RuntimeException("Пачка обработана не полностью, offset не подтвержден");
        }
    }

    public void processMessage(TransferMessage message) {
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

        executeTransfer(message);
    }

    private void executeTransfer(TransferMessage message) {
        try {
            transactionTemplate.executeWithoutResult(status -> {
                boolean withdrawn = accountDao.withdrawIfEnough(
                        message.getFromAccountId(),
                        message.getAmount()
                );

                if (!withdrawn) {
                    throw new IllegalArgumentException(
                            "Недостаточно средств на счете " + message.getFromAccountId()
                    );
                }

                boolean deposited = accountDao.deposit(
                        message.getToAccountId(),
                        message.getAmount()
                );

                if (!deposited) {
                    throw new IllegalStateException(
                            "Не удалось зачислить деньги на счет " + message.getToAccountId()
                    );
                }

                Transfer transfer = new Transfer(
                        message.getId(),
                        message.getFromAccountId(),
                        message.getToAccountId(),
                        message.getAmount(),
                        TransferStatus.READY
                );

                transferDao.save(transfer);
            });

            log.info("Успешно обработан перевод id={}", message.getId());

        } catch (IllegalArgumentException e) {
            log.error("Ошибка валидации при обработке перевода id={}: {}", message.getId(), e.getMessage());
            saveErrorTransfer(message);

        } catch (DataIntegrityViolationException e) {
            log.info("Перевод с id={} уже существует, повторная обработка пропущена", message.getId());

        } catch (Exception e) {
            log.error("Сбой при обновлении балансов для перевода id={}", message.getId(), e);
            saveErrorTransfer(message);
            throw new RuntimeException("Ошибка обработки перевода id=" + message.getId(), e);
        }
    }

    private void saveErrorTransfer(TransferMessage message) {
        try {
            transactionTemplate.executeWithoutResult(status -> {
                Transfer errorTransfer = new Transfer(
                        message.getId(),
                        message.getFromAccountId(),
                        message.getToAccountId(),
                        message.getAmount(),
                        TransferStatus.ERROR
                );

                transferDao.save(errorTransfer);
            });
        } catch (DataIntegrityViolationException e) {
            log.info("Запись о переводе id={} уже существует, повторное сохранение ERROR пропущено", message.getId());
        }
    }
}