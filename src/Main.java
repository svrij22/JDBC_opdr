import dao.*;
import domein.Adres;
import domein.OVChipKaart;
import domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

class Main {
    private static Connection conn;

    public static void main(String[] args){
        try {
            Main.getConnection();
            Main.opdracht1();

            //Create new
            ReizigerDAOPsql DAOS = new ReizigerDAOPsql(conn);
            AdresDAOPsql AAOS = new AdresDAOPsql(conn);
            List<Reiziger> alleReizigers = DAOS.findAll(true);
            for (Reiziger r : alleReizigers){
                System.out.println(r.toString());
            }

            //Test if exists
            System.out.println(DAOS.idExists(1));
            System.out.println(DAOS.idExists(2));
            System.out.println(DAOS.idExists(3));
            System.out.println(DAOS.idExists(77));

            //Print aantal
            System.out.println("Aantal reizigers " + DAOS.findAll(true).size());

            //Maak reiziger aan
            Reiziger reiz1 = new Reiziger(9, "B", "de", "Bouwer", Date.valueOf("1800-03-02"));
            Reiziger reiz2 = new Reiziger(10, "P", "de", "Clown", Date.valueOf("1800-03-02"));
            Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf("1800-03-02"));

            //Test new reizigers
            try{
                DAOS.save(reiz1);
                DAOS.save(reiz2);

                //Print aantal
                System.out.println("Aantal reizigers (+2) " + DAOS.findAll(true).size());

            } catch (Exception e) {
                e.printStackTrace();
            }

            //Test update reizigers
            DAOS.update(new Reiziger(1, "M", "", "Loe", Date.valueOf("1800-03-02")));
            DAOS.update(new Reiziger(2, "T", "de", "Stoomlocomotief", Date.valueOf("1800-03-02")));

            //Delete ze weer
            DAOS.delete(reiz1);
            DAOS.delete(reiz2);

            //Print aantal
            System.out.println("Aantal reizigers " + DAOS.findAll(true).size());

            //Overige tests
            DAOS.delete(sietske); // debug

            testReizigerDAO(DAOS);
            testAdresDAO(AAOS);
            DAOS.delete(sietske);

            //Test OVChipkaartDAO
            OVChipkaartDAOPsql OVC_DAOSql = new OVChipkaartDAOPsql(conn);
            for (OVChipKaart ovc : OVC_DAOSql.findAll()){
                System.out.println(ovc);
            }

            //New test
            System.out.println("gevonden reiziger " + DAOS.findByGb("1800-03-02", true));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void getConnection() throws SQLException {

        //Try connecting to database
        System.out.println("Attempting to get connection");

        //Set url
        String url = "jdbc:postgresql://localhost/ovchip";

        //Create properties object
        Properties props = new Properties();
        props.setProperty("user","ov");
        props.setProperty("password","ov");
        conn = DriverManager.getConnection(url, props);
    }

    public static void opdracht1() throws SQLException {
        Statement stmt = conn.createStatement();

        String sql = "SELECT * FROM reiziger";
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()){
            String[] keys = new String[]{"reiziger_id", "voorletters", "tussenvoegsel", "achternaam", "geboortedatum"};
            ArrayList<String> vals = new ArrayList<>();
            for (String key : keys) vals.add((rs.getString(key) != null) ? rs.getString(key) : "");
            System.out.printf("#%s: %s. %s %s (%s) \n", vals.get(0), vals.get(1), vals.get(2), vals.get(3), vals.get(4));
        }
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws Exception {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll(true);
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll(true);
        System.out.println(reizigers.size() + " reizigers\n");
    }

    private static void testAdresDAO(AdresDAO adao) throws Exception {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Adres> adressen = adao.findAll(true);
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();
    }
}