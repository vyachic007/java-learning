package by.slava_borisov.consumer.dao.impl;

import by.slava_borisov.consumer.dao.TransferDao;
import by.slava_borisov.consumer.model.Transfer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class TransferDaoImpl implements TransferDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(Transfer transfer) {
        entityManager.persist(transfer);
    }

    @Override
    public boolean isExistsById(String id) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(t) FROM Transfer t WHERE t.id = :id"
        );
        query.setParameter("id", id);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }
}