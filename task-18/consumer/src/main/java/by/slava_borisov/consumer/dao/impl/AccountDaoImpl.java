package by.slava_borisov.consumer.dao.impl;

import by.slava_borisov.consumer.dao.AccountDao;
import by.slava_borisov.consumer.model.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class AccountDaoImpl implements AccountDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Account> findById(Long accountId) {
        Account account = entityManager.find(Account.class, accountId);
        return Optional.ofNullable(account);
    }

    @Override
    @Transactional
    public void updateBalance(Long accountId, BigDecimal newBalance) {
        Query query = entityManager.createQuery(
                "UPDATE Account a SET a.balance = :newBalance WHERE a.id = :accountId"
        );
        query.setParameter("newBalance", newBalance);
        query.setParameter("accountId", accountId);
        query.executeUpdate();
    }
}