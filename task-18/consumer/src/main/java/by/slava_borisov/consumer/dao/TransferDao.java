package by.slava_borisov.consumer.dao;

import by.slava_borisov.consumer.model.Transfer;

public interface TransferDao {

    void createTablesIfNotExist();

    void save(Transfer transfer);

    boolean isExistsById(String id);
}
