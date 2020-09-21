package ovchip_hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.sql.ordering.antlr.Factory;
import ovchip_hibernate.dao.ProductDAOHibernate;
import ovchip_hibernate.dao.ReizigerDAOHibernate;
import ovchip_hibernate.domein.OVChipKaart;
import ovchip_hibernate.domein.Product;
import ovchip_hibernate.domein.Reiziger;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.sql.Date;
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

    /**
     * Retouneer een Hibernate session.
     *
     * @return Hibernate session
     * @throws HibernateException
     */

    public static void main(String[] args) throws SQLException {
        testFetchAll();
        testDAO2Hiberate();
        testDAOHibernate();
    }

    private static void testDAO2Hiberate() throws SQLException {
        /*Open factory*/
        SessionFactory factory = HibernateService.getFactory();
        Session session = HibernateService.getCurrentSession();
        Metamodel metamodel = session.getSessionFactory().getMetamodel();
        ProductDAOHibernate DAO = new ProductDAOHibernate();

        /*Product zoeken obv ovc*/
        OVChipKaart ovChipKaart = new OVChipKaart(35283, 2, 2, Date.valueOf("2022-03-03"), 22.22);
        List<Product> prods = DAO.findByOV(ovChipKaart);
        System.out.println("Gevonden prods voor OVC 35283" + prods);

    }

    private static void testDAOHibernate() {

        try{
            /*Open factory*/
            SessionFactory factory = HibernateService.getFactory();
            Session session = HibernateService.getCurrentSession();

            Metamodel metamodel = session.getSessionFactory().getMetamodel();
            ReizigerDAOHibernate RDAOH = new ReizigerDAOHibernate();

            /*Reizigers lijst*/
            System.out.println("[Test] Alle objecten van type Reiziger uit database:");
            List<Reiziger> reiz = RDAOH.findAll();
            for (Reiziger r : reiz){
                System.out.println(r);
            }

            /*Reiziger vinden*/
            Reiziger gevondenReiziger = RDAOH.findByGb("1998-08-11");
            System.out.println("Gevonden reiziger: " + gevondenReiziger);

            /*Reiziger toevoegen*/
            System.out.println("Reizigers aantal: " + reiz.size());
            Reiziger newReiziger = new Reiziger(24, "S","J","Vrij", Date.valueOf("2001-09-09"));
            RDAOH.save(newReiziger);
            reiz = RDAOH.findAll();
            System.out.println("Reizigers aantal na toevoegen: " + reiz.size());

            /*Reiziger aanpassen*/
            System.out.println("Reiziger geboortedatum: " + newReiziger.getGeboortedatum());
            newReiziger.setGeboortedatum(Date.valueOf("2001-09-03"));
            RDAOH.update(newReiziger);
            newReiziger = RDAOH.findByGb("2001-09-03");
            System.out.println("Reiziger gevonden: " + newReiziger);

            /*Reiziger weer verwijderen*/
            RDAOH.delete(newReiziger);
            reiz = RDAOH.findAll();
            System.out.println("Reizigers aantal na verwijderen: " + reiz.size());

            session.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * P6. Haal alle (geannoteerde) entiteiten uit de database.
     */
    private static void testFetchAll() {
        /*Open factory*/
        SessionFactory factory = HibernateService.getFactory();
        Session session = HibernateService.getCurrentSession();

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