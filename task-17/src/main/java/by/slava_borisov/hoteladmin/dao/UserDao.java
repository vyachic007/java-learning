package by.slava_borisov.hoteladmin.dao;

import by.slava_borisov.hoteladmin.model.User;

import java.util.Optional;

public interface UserDao extends GenericDao<User, Long> {

    Optional<User> findByUsername(String name);
}
