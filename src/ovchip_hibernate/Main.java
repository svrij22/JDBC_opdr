package ovchip_hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ovchip_hibernate.dao.*;
import ovchip_hibernate.domein.Adres;
import ovchip_hibernate.domein.OVChipKaart;
import ovchip_hibernate.domein.Product;
import ovchip_hibernate.domein.Reiziger;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.sql.Date;
import java.sql.SQLException;
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
        testProductDAOHibernate();
        testReizigerDAOHibernate();
        testOVCDAOHibernate();
        testAdresDAOHibernate();
        System.out.println("Lazy testing ik weet het :)");
    }

    private static void testProductDAOHibernate()  {
        /*Open factory*/
        SessionFactory factory = HibernateService.getFactory();
        Session session = HibernateService.getCurrentSession();
        Metamodel metamodel = session.getSessionFactory().getMetamodel();
        ProductDAOHibernate DAO = new ProductDAOHibernate();

        /*Product zoeken obv ovc*/
        OVChipKaart ovChipKaart = new OVChipKaart(35283, 2, 2, Date.valueOf("2022-03-03"), 22.22);
        List<Product> prods = DAO.findByOV(ovChipKaart);
        System.out.println("Gevonden prods voor OVC 35283" + prods);

        /*Product lijst*/
        System.out.println("[Test] Alle objecten van type Producten uit database:");
        prods = DAO.findAll();
        for (Product prod : prods){
            System.out.println(prod);
        }

        /*Product toevoegen*/
        System.out.println("[Test]");
        System.out.println("Producten aantal: " + prods.size());
        Product newProd = new Product(7, "test", "test", 34.43);
        DAO.save(newProd);
        prods = DAO.findAll();
        System.out.println("Producten aantal na toevoegen: " + prods.size());

        /*Product weer verwijderen*/
        System.out.println("[Test]");
        DAO.delete(newProd);
        prods = DAO.findAll();
        System.out.println("Producten aantal na verwijderen: " + prods.size());

    }

    private static void testOVCDAOHibernate(){
        /*Open factory*/
        SessionFactory factory = HibernateService.getFactory();
        Session session = HibernateService.getCurrentSession();
        Metamodel metamodel = session.getSessionFactory().getMetamodel();
        OVChipkaartDAOHibernate DAO = new OVChipkaartDAOHibernate();

        /*Zoeken op nummer*/
        OVChipKaart ovc = DAO.findByNummer(35283);
        System.out.println("[Test] Zoek OVChipkaart op nummer: " + ovc.toString());

        /*OVC lijst*/
        System.out.println("[Test] Alle objecten van type OVChipkaart uit database:");
        List<OVChipKaart> ovcs = DAO.findAll();
        for (OVChipKaart ov : ovcs){
            System.out.println(ov);
        }

        /*OVC lijst*/
        Reiziger reiz = new Reiziger(3, "H", "", "Lubben", Date.valueOf("1998-08-11"));
        System.out.println("[Test] Alle objecten van type OVChipkaart uit database voor reiziger " + reiz.toString() + " :");
        ovcs = DAO.findByReiziger(reiz);
        for (OVChipKaart ov : ovcs){
            System.out.println(ov);
        }

    }

    private static void testAdresDAOHibernate(){
        /*Open factory*/
        SessionFactory factory = HibernateService.getFactory();
        Session session = HibernateService.getCurrentSession();
        Metamodel metamodel = session.getSessionFactory().getMetamodel();
        AdresDAOHibernate DAO = new AdresDAOHibernate();

        /*Lijst*/
        System.out.println("[Test] Alle objecten van type Adres uit database:");
        List<Adres> adressen = DAO.findAll();
        for (Adres adres : adressen){
            System.out.println(adres);
        }

        /*Adres by id*/
        System.out.println("[Test] zoek adres op ID (5):");
        Adres adres = DAO.findById(5);
        System.out.println(adres.toString());

        /*Zoek op reiziger*/
        Reiziger reiz = new Reiziger(3, "H", "", "Lubben", Date.valueOf("1998-08-11"));
        System.out.println("[Test] zoek adres op reiziger:" + reiz.toString());
        adres = DAO.findByReiziger(reiz);
        System.out.println(adres);
    }

    private static void testReizigerDAOHibernate() {

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
            System.out.println("[Test]");
            System.out.println("Reizigers aantal: " + reiz.size());
            Reiziger newReiziger = new Reiziger(24, "S","J","Vrij", Date.valueOf("2001-09-09"));
            RDAOH.save(newReiziger);
            reiz = RDAOH.findAll();
            System.out.println("Reizigers aantal na toevoegen: " + reiz.size());

            /*Reiziger aanpassen*/
            System.out.println("[Test]");
            System.out.println("Reiziger geboortedatum: " + newReiziger.getGeboortedatum());
            newReiziger.setGeboortedatum(Date.valueOf("2001-09-03"));
            RDAOH.update(newReiziger);
            newReiziger = RDAOH.findByGb("2001-09-03");
            System.out.println("Reiziger gevonden: " + newReiziger);

            /*Reiziger weer verwijderen*/
            System.out.println("[Test]");
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