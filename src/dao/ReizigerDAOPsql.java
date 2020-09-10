package dao;

import domein.Adres;
import domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class ReizigerDAOPsql implements ReizigerDAO {
    Connection connection;
    static ReizigerDAOPsql DAO;

    public ReizigerDAOPsql(Connection connection) {
        this.connection = connection;
        DAO = this;
    }

    @Override
    public boolean save(Reiziger reiziger) throws Exception {

        //Already exists
        if (this.idExists(reiziger.getReiziger_id())){
            throw new Exception("Reiziger met deze ID bestaat al.");
        }

        //Create statement
        PreparedStatement stmt = connection.prepareStatement("insert into reiziger values (?, ?, ?, ?, ?)"); // Create statement
        stmt.setInt(1, reiziger.getReiziger_id());
        stmt.setString(2, reiziger.getVoorletters());
        stmt.setString(3, reiziger.getTussenvoegsel());
        stmt.setString(4, reiziger.getAchternaam());
        stmt.setDate(5, reiziger.getDate());

        //Return result
        return stmt.execute();
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        //Create statement
        PreparedStatement stmt = connection.prepareStatement("UPDATE reiziger SET voorletters=?, tussenvoegsel=?, achternaam=?, geboortedatum=? WHERE reiziger_id=?");

        stmt.setString(1, reiziger.getVoorletters());
        stmt.setString(2, reiziger.getTussenvoegsel());
        stmt.setString(3, reiziger.getAchternaam());
        stmt.setDate(4, reiziger.getDate());
        stmt.setInt(5, reiziger.getReiziger_id());

        //Execute and return value
        return stmt.execute();
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        //Create statement
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM reiziger WHERE reiziger_id=?");
        stmt.setInt(1, reiziger.getReiziger_id());
        return stmt.execute();
    }

    public boolean idExists(int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM reiziger WHERE reiziger_id=?"); // Create statement
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery(); // Get results

        rs.next();
        try {
            return (rs.getInt("reiziger_id") == id);
        }catch (Exception ignored){}
        return false;
    }

    @Override
    public Reiziger findById(int id, boolean link) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM reiziger WHERE reiziger_id=?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery(); // Get results
        return reizigerRsToList(rs, link).get(0);
    }

    @Override
    public List<Reiziger> findByGb(String datum, boolean link) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM reiziger WHERE geboortedatum=?");
        stmt.setDate(1,Date.valueOf(datum));
        ResultSet rs = stmt.executeQuery(); // Get results
        return reizigerRsToList(rs, link);
    }

    @Override
    public List<Reiziger> findAll(boolean link) throws SQLException {
        Statement stmt = connection.createStatement(); // Create statement
        String sql = "SELECT * FROM reiziger";
        ResultSet rs = stmt.executeQuery(sql); // Get results
        return reizigerRsToList(rs, link);
    }

    public List<Reiziger> reizigerRsToList(ResultSet rs, boolean link) throws SQLException {
        ArrayList<Reiziger> reizigers = new ArrayList<>();

        //While has next reiziger.
        while (rs.next()){

            //Link the values to a new object
            Reiziger reiz = new Reiziger(
                    rs.getInt("reiziger_id"),
                    rs.getString("voorletters"),
                    rs.getString("tussenvoegsel"),
                    rs.getString("achternaam"),
                    rs.getDate("geboortedatum")
            );

            //Links the reiziger and adres
            if (link){
                //Get adres and link both ways
                Adres adres = AdresDAOPsql.DAO.findByReiziger(reiz, false);
                if (adres != null) adres.setReiziger(reiz);
                reiz.setAdres(adres);
            }

            //Add to list
            reizigers.add(reiz);
        }

        return reizigers;
    }
}
