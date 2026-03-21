package by.slava_borisov.consumer.dao;

import by.slava_borisov.consumer.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    void createTableIfNotExist();

    Account findById(Long accountId);

    void updateBalance(Long accountId, BigDecimal newBalance);
}
