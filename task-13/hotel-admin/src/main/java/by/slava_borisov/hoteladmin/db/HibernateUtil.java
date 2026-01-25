package by.slava_borisov.hoteladmin.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;
    private static final ThreadLocal<Session> threadSession = new ThreadLocal<>();
    private static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<>();

    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
            log.info("SessionFactory успешно создана");
        } catch (Exception e) {
            log.error("Ошибка при создании SessionFactory", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Session getSession() {
        Session session = threadSession.get();
        if (session == null || !session.isOpen()) {
            session = sessionFactory.openSession();
            threadSession.set(session);
        }
        return session;
    }

    public static void beginTransaction() {
        Transaction tx = threadTransaction.get();
        if (tx == null || !tx.isActive()) {
            tx = getSession().beginTransaction();
            threadTransaction.set(tx);
            log.debug("Транзакция начата");
        }
    }

    public static void commit() {
        Transaction tx = threadTransaction.get();
        if (tx != null && tx.isActive()) {
            tx.commit();
            threadTransaction.remove();
            log.debug("Транзакция закоммичена");
        }
        closeSession();
    }

    public static void rollback() {
        Transaction tx = threadTransaction.get();
        if (tx != null && tx.isActive()) {
            tx.rollback();
            threadTransaction.remove();
            log.debug("Транзакция откатана");
        }
        closeSession();
    }

    public static void closeSession() {
        Session session = threadSession.get();
        if (session != null && session.isOpen()) {
            session.close();
            threadSession.remove();
            log.debug("Session закрыта");
        }
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            log.info("SessionFactory закрыта");
        }
    }
}
