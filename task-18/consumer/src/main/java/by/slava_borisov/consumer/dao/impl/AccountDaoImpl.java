package by.slava_borisov.consumer.dao.impl;

import by.slava_borisov.consumer.dao.AccountDao;
import by.slava_borisov.consumer.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@RequiredArgsConstructor
public class AccountDaoImpl implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createTableIfNotExist() {
        String sql = """
                CREATE TABLE IF NOT EXISTS transfers (
                                id VARCHAR(36) PRIMARY KEY,
                                from_account_id BIGINT NOT NULL,
                                to_account_id BIGINT NOT NULL,
                                amount DECIMAL(19,2) NOT NULL,
                                status VARCHAR(20) NOT NULL
                            )
                """;

        jdbcTemplate.execute(sql);
    }

    @Override
    public Account findById(Long accountId) {
        return null;
    }

    @Override
    public void updateBalance(Long accountId, BigDecimal newBalance) {

    }
}
