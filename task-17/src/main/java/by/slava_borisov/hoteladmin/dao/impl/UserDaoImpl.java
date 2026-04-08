package by.slava_borisov.hoteladmin.dao.impl;

import by.slava_borisov.hoteladmin.dao.UserDao;
import by.slava_borisov.hoteladmin.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final SessionFactory sessionFactory;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        log.debug("Поиск по имени: {}", username);
        return session().createQuery("" +
                        "SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public User create(User user) {
        session().persist(user);
        log.debug("Пользователь создан: {}", user.getUsername());
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        log.debug("Поиск по id={}", id);
        return Optional.ofNullable(session().find(User.class, id));
    }

    @Override
    public List<User> findAll() {
        return session().createQuery("SELECT u FROM User u", User.class)
                .list();
    }

    @Override
    public User update(User user) {
        User merged = session().merge(user);
        log.debug("Пользователь обновлен: {}", user.getUsername());
        return merged;
    }

    @Override
    public boolean deleteById(Long id) {
        User user = session().find(User.class, id);
        if (user != null) {
            session().remove(user);
            log.debug("Пользователь удален: {}", user.getUsername());
            return true;
        }
        return false;
    }
}
