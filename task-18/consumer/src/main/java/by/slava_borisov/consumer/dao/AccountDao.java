package by.slava_borisov.consumer.dao;

import by.slava_borisov.consumer.model.Account;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountDao {

    Optional<Account> findById(Long accountId);

    boolean withdrawIfEnough(Long accountId, BigDecimal amount);

    boolean deposit(Long accountId, BigDecimal amount);
}