package by.slava_borisov.producer.initializer;

import by.slava_borisov.producer.dao.AccountDao;
import by.slava_borisov.producer.model.Account;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountDataInitializer {

    private final AccountDao accountDao;
    @Getter
    private final Map<Long, Account> accountMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("Запуск инициализации данных счетов");

        accountDao.createTablesIfNotExist();

        if (accountDao.isEmpty()) {
            log.info("В базе данных не найдено счетов. Генерируем 1000 счетов...");
            generateAndSaveAccounts();
        } else {
            log.info("Счета уже существуют в базе данных");
        }

        loadAccountsToCache();

        log.info("Инициализация данных счетов завершена. Загружено {} счетов", accountMap.size());
    }

    private void generateAndSaveAccounts() {
        List<Account> accounts = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            accounts.add(new Account(BigDecimal.valueOf(1_000_000)));
        }

        accountDao.saveAll(accounts);
        log.info("Сгенерировано и сохранено 1000 счетов в базе данных");
    }

    private void loadAccountsToCache() {
        Map<Long, Account> accountsFromDb = accountDao.findAllAsMap();
        accountMap.putAll(accountsFromDb);
    }
}
