package by.slava_borisov.producer.dao.impl;

import by.slava_borisov.producer.dao.AccountDao;
import by.slava_borisov.producer.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AccountDaoImpl implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public AccountDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void createTablesIfNotExist() {
        String createAccount = """
                CREATE TABLE IF NOT EXISTS accounts (
                    id BIGSERIAL PRIMARY KEY,
                    balance DECIMAL(19,2) NOT NULL
                )
                """;

        jdbcTemplate.execute(createAccount);
    }

    @Override
    public boolean isEmpty() {
        String sql = "SELECT COUNT(*) FROM accounts";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null && count == 0;
    }

    @Override
    public void saveAll(List<Account> accounts) {
        String sql = "INSERT INTO accounts (balance) VALUES (?)";

        jdbcTemplate.batchUpdate(sql, accounts, accounts.size(),
                (PreparedStatement ps, Account account) -> {
                    ps.setBigDecimal(1, account.getBalance());
                });
    }

    @Override
    public List<Account> findAll() {
       String sql = "SELECT id, balance FROM accounts";

        return jdbcTemplate.query(sql, (ResultSet rs) -> {
            List<Account> accounts = new ArrayList<>();
            while (rs.next()) {
                Account account = new Account();
                account.setId(rs.getLong("id"));
                account.setBalance(rs.getBigDecimal("balance"));
                accounts.add(account);
            }
            return accounts;
        });
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
