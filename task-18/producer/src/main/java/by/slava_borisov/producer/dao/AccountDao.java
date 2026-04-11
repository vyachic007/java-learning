package by.slava_borisov.producer.dao;

import by.slava_borisov.producer.model.Account;

import java.util.List;
import java.util.Map;

public interface AccountDao {

    boolean isEmpty();

    void saveAll(List<Account> accounts);

    List<Account> findAll();

    Map<Long, Account> findAllAsMap();
}
