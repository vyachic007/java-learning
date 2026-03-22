package by.slava_borisov.producer.service;

import by.slava_borisov.producer.initializer.AccountDataInitializer;
import by.slava_borisov.producer.model.TransferMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AccountDataInitializer accountDataInitializer;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "transfers";

    @Transactional("kafkaTransactionManager")
    @Scheduled(fixedDelay = 200)
    public void generateAndSend() {

        try {
            List<Long> accountIds = new ArrayList<>(accountDataInitializer.getAccountMap().keySet());

            Collections.shuffle(accountIds);
            Long fromId = accountIds.get(0);
            Long toId = accountIds.get(1);

            if (fromId.equals(toId) && accountIds.size() > 2) {
                toId = accountIds.get(2);
            }

            BigDecimal amount = BigDecimal.valueOf(
                    ThreadLocalRandom.current().nextInt(1, 10001)
            );

            String transferId = UUID.randomUUID().toString();

            TransferMessage message = new TransferMessage(
                    transferId, fromId, toId, amount
            );

            String jsonMessage = objectMapper.writeValueAsString(message);

            ProducerRecord<String, String> record =
                    new ProducerRecord<>(TOPIC, transferId, jsonMessage);

            kafkaTemplate.executeInTransaction(operations -> {

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
                                message.getAmount(), ex);
                    }
                });

                return true;
            });

        } catch (Exception e) {
            log.error("Ошибка при генерации и отправке сообщения", e);
        }
    }
}