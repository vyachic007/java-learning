package by.slava_borisov.hoteladmin.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public abstract class AbstractHibernateDao<T, ID> {

    private final SessionFactory sessionFactory;
    private final Class<T> entityClass;

    protected AbstractHibernateDao(SessionFactory sessionFactory, Class<T> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
    }

    protected Session session() {
        return sessionFactory.getCurrentSession();
    }

    public T create(T entity) {
        session().persist(entity);
        return entity;
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(session().find(entityClass, id));
    }

    public List<T> findAll() {
        return session().createSelectionQuery("FROM " + entityClass.getSimpleName(), entityClass).list();
    }

    public T update(T entity) {
        return session().merge(entity);
    }

    public boolean deleteById(ID id) {
        T entity = session().find(entityClass, id);
        if (entity == null) {
            return false;
        }
        session().remove(entity);
        return true;
    }
}