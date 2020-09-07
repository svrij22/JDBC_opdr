package dao;

import domein.Adres;
import domein.Reiziger;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class reizigerDAOSql implements reizigerDAO{
    Connection connection;
    ArrayList<Reiziger> reizigers = new ArrayList<>();

    public reizigerDAOSql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(Reiziger reiziger) throws Exception {
        //Already exists
        if (this.idExists(reiziger.getReiziger_id())){
            throw new Exception("Reiziger met deze ID bestaat al.");
        }

        //Voeg toe aan lijst
        reizigers.add(reiziger);

        //Create statement
        Statement stmt = connection.createStatement(); // Create statement
        String sql = String.format("insert into reiziger values (%s, '%s', '%s', '%s', '%s')",
                reiziger.getReiziger_id(),
                reiziger.getVoorletters(),
                reiziger.getTussenvoegsel(),
                reiziger.getAchternaam(),
                reiziger.getDate().toString());

        //Return result
        return stmt.execute(sql);
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        //Update by replacing
        Reiziger orig = this.findById(reiziger.getReiziger_id());
        reizigers.remove(orig);
        reizigers.add(reiziger);

        //Create statement
        Statement stmt = connection.createStatement(); // Create statement
        String sql = String.format("UPDATE reiziger SET " +
                        "voorletters='%s', " +
                        "tussenvoegsel='%s', " +
                        "achternaam='%s', " +
                        "geboortedatum='%s' " +
                        "WHERE reiziger_id='%s'",
                reiziger.getVoorletters(),
                reiziger.getTussenvoegsel(),
                reiziger.getAchternaam(),
                reiziger.getDate().toString(),
                reiziger.getReiziger_id());
        return stmt.execute(sql);
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        //Remove from list
        reizigers.remove(reiziger);

        //Create statement
        Statement stmt = connection.createStatement(); // Create statement
        String sql = String.format("DELETE FROM reiziger WHERE reiziger_id='%s'", reiziger.getReiziger_id());
        return stmt.execute(sql);
    }

    public boolean idExists(int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM reiziger WHERE reiziger_id=?"); // Create statement
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery(); // Get results

        rs.next();
        boolean exists = false;
        try {
            exists = (rs.getInt("reiziger_id") == id);
        } finally {
            return exists;
        }
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        for (Reiziger r : this.findAll()) {
            if (r.getReiziger_id() == id) return r;
        }
        return null;
    }

    @Override
    public List<Reiziger> findByGb(String datum) throws SQLException {
        ArrayList<Reiziger> gevondenReizigers = new ArrayList<>();
        for (Reiziger r : this.findAll()) {
            if (r.getDate().toString().equals(datum)) gevondenReizigers.add(r);
        }
        return gevondenReizigers;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        reizigers.clear(); // Clear original list
        Statement stmt = connection.createStatement(); // Create statement
        String sql = "SELECT * FROM reiziger";
        ResultSet rs = stmt.executeQuery(sql); // Get results

        while (rs.next()){

            //Get alle key and value pairs from database
            String[] keys = new String[]{"reiziger_id", "voorletters", "tussenvoegsel", "achternaam", "geboortedatum"};
            ArrayList<String> vals = new ArrayList<>();
            for (String key : keys) vals.add(rs.getString(key));

            //Link the values to a new object
            Reiziger reiz = new Reiziger(parseInt(vals.get(0)),
                    vals.get(1), vals.get(2), vals.get(3),
                    Date.valueOf(vals.get(4))
            );

            //Sets the reiziger adress
            List<Adres> adres = adresDAOSql.findByReiziger(reiz);
            reiz.setAdres((adres.size() > 0) ? adres.get(0) : null);

            //Add to list
            reizigers.add(reiz);
        }

        return reizigers;
    }
}
