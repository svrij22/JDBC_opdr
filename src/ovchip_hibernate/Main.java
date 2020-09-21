package ovchip_hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ovchip_hibernate.dao.ReizigerDAOHibernate;
import ovchip_hibernate.domein.Reiziger;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Testklasse - deze klasse test alle andere klassen in deze package.
 *
 * System.out.println() is alleen in deze klasse toegestaan (behalve voor exceptions).
 *
 * @author tijmen.muller@hu.nl
 */
public class Main {
    // Creëer een factory voor Hibernate sessions.
    private static final SessionFactory factory;

    static {
        try {
            // Create a Hibernate session factory
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Retouneer een Hibernate session.
     *
     * @return Hibernate session
     * @throws HibernateException
     */
    private static Session getSession() throws HibernateException {
        return factory.openSession();
    }

    public static void main(String[] args) throws SQLException {
        testFetchAll();
        testDAOHibernate();
    }

    private static void testDAOHibernate() throws SQLException {

        Session session = getSession();
        Metamodel metamodel = session.getSessionFactory().getMetamodel();

        ReizigerDAOHibernate RDAOH = new ReizigerDAOHibernate(session);

        System.out.println("[Test] Alle objecten van type Reiziger uit database:");

        List<Reiziger> reiz = RDAOH.findAll();
        for (Reiziger r : reiz){
            System.out.println(r);
        }

        Reiziger gevondenReiziger = RDAOH.findByGb("1998-08-11");
        System.out.println("Gevonden reiziger: " + gevondenReiziger);

        session.close();
    }

    /**
     * P6. Haal alle (geannoteerde) entiteiten uit de database.
     */
    private static void testFetchAll() {
        Session session = getSession();
        try {
            Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                Query query = session.createQuery("from " + entityType.getName());

                System.out.println("[Test] Alle objecten van type " + entityType.getName() + " uit database:");
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
                System.out.println();
            }
        } finally {
            session.close();
        }
    }
}