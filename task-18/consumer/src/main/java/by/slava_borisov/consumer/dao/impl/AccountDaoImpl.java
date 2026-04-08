package by.slava_borisov.consumer.dao.impl;

import by.slava_borisov.consumer.dao.AccountDao;
import by.slava_borisov.consumer.model.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional("transactionManager")
    public boolean withdrawIfEnough(Long accountId, BigDecimal amount) {
        Query query = entityManager.createQuery("""
                UPDATE Account a
                SET a.balance = a.balance - :amount
                WHERE a.id = :accountId AND a.balance >= :amount
                """);
        query.setParameter("amount", amount);
        query.setParameter("accountId", accountId);

        return query.executeUpdate() == 1;
    }

    @Override
    @Transactional("transactionManager")
    public boolean deposit(Long accountId, BigDecimal amount) {
        Query query = entityManager.createQuery("""
                UPDATE Account a
                SET a.balance = a.balance + :amount
                WHERE a.id = :accountId
                """);
        query.setParameter("amount", amount);
        query.setParameter("accountId", accountId);

        return query.executeUpdate() == 1;
    }
}