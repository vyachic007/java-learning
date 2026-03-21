package by.slava_borisov.producer.dao.impl;

import by.slava_borisov.producer.dao.AccountDao;
import by.slava_borisov.producer.model.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AccountDaoImpl implements AccountDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isEmpty() {
        Query query = entityManager.createQuery("SELECT COUNT(a) FROM Account a");
        Long count = (Long) query.getSingleResult();
        return count == 0;
    }

    @Override
    @Transactional
    public void saveAll(List<Account> accounts) {
        for (Account account : accounts) {
            entityManager.persist(account);
        }
        entityManager.flush();
    }

    @Override
    public List<Account> findAll() {
        Query query = entityManager.createQuery("SELECT a FROM Account a",
                Account.class);
        return query.getResultList();
    }

    @Override
    public Map<Long, Account> findAllAsMap() {
        List<Account> accounts = findAll();
        Map<Long, Account> accountMap = new HashMap<>();
        for (Account account : accounts) {
            accountMap.put(account.getId(), account);
        }
        return accountMap;
    }
}