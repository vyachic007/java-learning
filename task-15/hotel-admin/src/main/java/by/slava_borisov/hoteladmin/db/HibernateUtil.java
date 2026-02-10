package by.slava_borisov.hoteladmin.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);

    private static final SessionFactory SESSION_FACTORY;
    private static final ThreadLocal<Session> THREAD_SESSION = new ThreadLocal<>();
    private static final ThreadLocal<Transaction> THREAD_TRANSACTION = new ThreadLocal<>();

    static {
        try {
            SESSION_FACTORY = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
            log.info("SessionFactory успешно создана");
        } catch (Exception e) {
            log.error("Ошибка при создании SessionFactory", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Session getSession() {
        Session session = THREAD_SESSION.get();
        if (session == null || !session.isOpen()) {
            session = SESSION_FACTORY.openSession();
            THREAD_SESSION.set(session);
        }
        return session;
    }

    public static void beginTransaction() {
        Transaction tx = THREAD_TRANSACTION.get();
        if (tx == null || !tx.isActive()) {
            tx = getSession().beginTransaction();
            THREAD_TRANSACTION.set(tx);
            log.debug("Транзакция начата");
        }
    }

    public static void commit() {
        Transaction tx = THREAD_TRANSACTION.get();
        if (tx != null && tx.isActive()) {
            tx.commit();
            THREAD_TRANSACTION.remove();
            log.debug("Транзакция закоммичена");
        }
        closeSession();
    }

    public static void rollback() {
        Transaction tx = THREAD_TRANSACTION.get();
        if (tx != null && tx.isActive()) {
            tx.rollback();
            THREAD_TRANSACTION.remove();
            log.debug("Транзакция откатана");
        }
        closeSession();
    }

    public static void closeSession() {
        Session session = THREAD_SESSION.get();
        if (session != null && session.isOpen()) {
            session.close();
            THREAD_SESSION.remove();
            log.debug("Session закрыта");
        }
    }

    public static void shutdown() {
        if (SESSION_FACTORY != null && !SESSION_FACTORY.isClosed()) {
            SESSION_FACTORY.close();
            log.info("SessionFactory закрыта");
        }
    }
}
