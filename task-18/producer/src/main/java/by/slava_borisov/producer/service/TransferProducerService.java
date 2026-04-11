package by.slava_borisov.producer.service;

import by.slava_borisov.producer.initializer.AccountDataInitializer;
import by.slava_borisov.producer.model.TransferMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferProducerService {

    private static final String TOPIC = "transfers";
    private static final int MESSAGES_PER_SECOND = 5;
    private static final int PARTITIONS_COUNT = 3;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AccountDataInitializer accountDataInitializer;
    private final ObjectMapper objectMapper;

    private final AtomicInteger partitionCounter = new AtomicInteger(0);

    @Scheduled(fixedDelay = 1000)
    public void generateAndSend() {
        try {
            kafkaTemplate.executeInTransaction(operations -> {
                for (int i = 0; i < MESSAGES_PER_SECOND; i++) {
                    sendOneMessage(operations);
                }
                return true;
            });
        } catch (Exception e) {
            log.error("Ошибка при генерации и отправке пачки сообщений", e);
        }
    }

    private void sendOneMessage(KafkaOperations<String, String> operations) {
        try {
            List<Long> accountIds = new ArrayList<>(accountDataInitializer.getAccountMap().keySet());

            if (accountIds.size() < 2) {
                log.error("Недостаточно счетов для генерации перевода");
                return;
            }

            Collections.shuffle(accountIds);

            Long fromId = accountIds.get(0);
            Long toId = accountIds.get(1);

            BigDecimal amount = BigDecimal.valueOf(
                    ThreadLocalRandom.current().nextInt(1, 10001)
            );

            String transferId = UUID.randomUUID().toString();

            TransferMessage message = new TransferMessage(
                    transferId,
                    fromId,
                    toId,
                    amount
            );

            String jsonMessage = objectMapper.writeValueAsString(message);

            int partition = Math.floorMod(partitionCounter.getAndIncrement(), PARTITIONS_COUNT);

            ProducerRecord<String, String> record =
                    new ProducerRecord<>(TOPIC, partition, transferId, jsonMessage);

            operations.send(record).whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Отправлено сообщение: id={}, со счета={}, на счет={}, сумма={}, партиция={}",
                            message.getId(),
                            message.getFromAccountId(),
                            message.getToAccountId(),
                            message.getAmount(),
                            result.getRecordMetadata().partition());
                } else {
                    log.error("Ошибка отправки сообщения: id={}, со счета={}, на счет={}, сумма={}",
                            message.getId(),
                            message.getFromAccountId(),
                            message.getToAccountId(),
                            message.getAmount(),
                            ex);
                }
            });

        } catch (Exception e) {
            log.error("Ошибка при генерации одного сообщения", e);
        }
    }
}