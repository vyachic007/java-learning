package by.slava_borisov.hoteladmin.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID> {

    T create(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    T update(T entity);

    boolean deleteById(ID id);
}
