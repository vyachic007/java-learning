CREATE TABLE IF NOT EXISTS accounts
(
    id      BIGSERIAL PRIMARY KEY,
    balance NUMERIC(19, 4) NOT NULL
);

CREATE TABLE IF NOT EXISTS transfers
(
    id              VARCHAR(255) PRIMARY KEY,
    from_account_id BIGINT         NOT NULL,
    to_account_id   BIGINT         NOT NULL,
    amount          NUMERIC(19, 4) NOT NULL,
    status          VARCHAR(50)    NOT NULL,
    CONSTRAINT fk_transfers_from_account
        FOREIGN KEY (from_account_id) REFERENCES accounts (id),
    CONSTRAINT fk_transfers_to_account
        FOREIGN KEY (to_account_id) REFERENCES accounts (id)
);