package ovchip_hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateService {

    private static Session currentSession;
    private static Transaction currentTransaction;
    private static SessionFactory factory;

    public static SessionFactory getFactory(){
        try {
            // Create a Hibernate session factory
            if (factory == null)
                factory = new Configuration().configure().buildSessionFactory();
            return factory;
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Session getSession() throws HibernateException {
        return factory.openSession();
    }

    private static void openSession(){
        currentSession = getSession();
    }

    public static Session getCurrentSession() {
        if (currentSession == null || !currentSession.isOpen())
        {
            openSession();
        }
        return currentSession;
    }

    public static void openTransaction(){
        if (currentTransaction == null)
            currentTransaction = currentSession.beginTransaction();
    }

    public static void commitTransaction(){
        currentTransaction.commit();
        currentTransaction = null;
    }
}
