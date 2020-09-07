package dao;

import domein.Adres;
import domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class adresDAOSql implements AdresDAO{
    static Connection connection;
    static ArrayList<Adres> adressen = new ArrayList<Adres>();

    public adresDAOSql(Connection connection) {
        adresDAOSql.connection = connection;
    }

    @Override
    public boolean save(Adres adres) throws Exception {

        //Already exists
        if (this.idExists(adres.getAdres_id())){
            throw new Exception("Adres met deze ID bestaat al.");
        }

        //Voeg toe aan lijst
        adressen.add(adres);

        //Create statement
        Statement stmt = connection.createStatement(); // Create statement
        String sql = String.format("insert into adres values (%s, '%s', '%s', '%s', '%s', %s)",
                adres.getAdres_id(),
                adres.getPostcode(),
                adres.getHuisnummer(),
                adres.getStraat(),
                adres.getWoonplaats(),
                adres.getReiziger_id());

        //Return result
        return stmt.execute(sql);
    }

    @Override
    public boolean update(Adres adres) throws SQLException {

        //Update by replacing
        Adres orig = this.findById(adres.getAdres_id());
        adressen.remove(orig);
        adressen.add(adres);

        //Create statement
        Statement stmt = connection.createStatement(); // Create statement
        String sql = String.format("UPDATE adres SET " +
                        "postcode='%s', " +
                        "huisnummer='%s', " +
                        "straat='%s', " +
                        "woonplaats='%s' " +
                        "reiziger_id=%s, " +
                        "WHERE adres_id='%s'",
                adres.getPostcode(),
                adres.getHuisnummer(),
                adres.getStraat(),
                adres.getWoonplaats(),
                adres.getReiziger_id(),
                adres.getAdres_id());
        return stmt.execute(sql);
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        //Remove from list
        adressen.remove(adres);

        //Create statement
        Statement stmt = connection.createStatement(); // Create statement
        String sql = String.format("DELETE FROM adres WHERE adres_id='%s'", adres.getAdres_id());
        return stmt.execute(sql);
    }

    public boolean idExists(int id) throws SQLException {
        Statement stmt = connection.createStatement(); // Create statement
        String sql = String.format("SELECT * FROM adres WHERE adres_id=%s", id);
        ResultSet rs = stmt.executeQuery(sql); // Get results

        rs.next();
        boolean exists = false;
        try {
            exists = (rs.getInt("adres_id") == id);
        }catch (Exception ignored){}
        return exists;
    }

    public Adres findById(int id) throws SQLException {
        for (Adres a : findAll()) {
            if (a.getAdres_id() == id) return a;
        }
        return null;
    }

    public static List<Adres> findByReiziger(Reiziger reiziger) throws SQLException {
        ArrayList<Adres> gevondenAdressen = new ArrayList<>();
        for (Adres a : findAll()) {
            if (a.getReiziger_id() == reiziger.getReiziger_id()) gevondenAdressen.add(a);
        }
        return gevondenAdressen;
    }

    public static ArrayList<Adres> findAll() throws SQLException {
        adressen.clear(); // Clear original list
        Statement stmt = connection.createStatement(); // Create statement
        String sql = "SELECT * FROM adres";
        ResultSet rs = stmt.executeQuery(sql); // Get results

        while (rs.next()){

            //Get alle key and value pairs from database
            String[] keys = new String[]{"adres_id", "postcode", "huisnummer", "straat", "woonplaats", "reiziger_id"};
            ArrayList<String> vals = new ArrayList<>();
            for (String key : keys) vals.add(rs.getString(key));

            //Link the values to a new object
            Adres adres = new Adres(parseInt(vals.get(0)), parseInt(vals.get(5)),
                    vals.get(1), vals.get(2), vals.get(3), vals.get(4));
            adressen.add(adres);
        }

        return adressen;
    }
}
