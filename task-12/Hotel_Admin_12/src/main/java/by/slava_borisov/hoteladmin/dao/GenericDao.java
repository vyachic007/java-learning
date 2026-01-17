package by.slava_borisov.hoteladmin.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID> {

    T create(T entity) throws SQLException;

    Optional<T> findById(ID id) throws SQLException;

    List<T> findAll() throws SQLException;

    T update(T entity) throws SQLException;

    boolean deleteById(ID id) throws SQLException;
}
