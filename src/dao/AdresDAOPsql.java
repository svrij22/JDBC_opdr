package dao;

import domein.Adres;
import domein.OVChipKaart;
import domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class AdresDAOPsql implements AdresDAO{
    Connection connection;
    static AdresDAOPsql DAO;

    public AdresDAOPsql(Connection connection) {
        this.connection = connection;
        DAO = this;
    }

    @Override
    public boolean save(Adres adres) throws Exception {

        //Already exists
        if (this.idExists(adres.getAdres_id())){
            throw new Exception("Adres met deze ID bestaat al.");
        }

        //Create statement
        PreparedStatement stmt = connection.prepareStatement("insert into adres values (?, ?, ?, ?, ?, ?)"); // Create statement
        stmt.setInt(1, adres.getAdres_id());
        stmt.setString(2, adres.getPostcode());
        stmt.setString(3, adres.getHuisnummer());
        stmt.setString(4, adres.getStraat());
        stmt.setString(5, adres.getWoonplaats());
        stmt.setInt(6, adres.getReiziger_id());

        //Return result
        return stmt.execute();
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        //Create statement
        PreparedStatement stmt = connection.prepareStatement("UPDATE adres SET postcode=?, huisnummer=?, " +
                "straat=?, woonplaats=?, reiziger_id=? WHERE adres_id=?");

        stmt.setString(1, adres.getPostcode());
        stmt.setString(2, adres.getHuisnummer());
        stmt.setString(3, adres.getStraat());
        stmt.setString(4, adres.getWoonplaats());
        stmt.setInt(5, adres.getReiziger_id());
        stmt.setInt(6, adres.getAdres_id());

        //Execute and return value
        return stmt.execute();
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        //Create statement
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM adres WHERE adres_id=?");
        stmt.setInt(1, adres.getAdres_id());
        return stmt.execute();
    }

    public boolean idExists(int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM adres WHERE adres_id=?"); // Create statement
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery(); // Get results

        rs.next();
        try {
            return (rs.getInt("adres_id") == id);
        }catch (Exception ignored){}
        return false;
    }

    @Override
    public Adres findById(int id, boolean link) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM adres WHERE adres_id=?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery(); // Get results
        return adresRsToList(rs, link).get(0);
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger, boolean link) throws SQLException {
        //Prepare statement and do query
        int reiziger_id = reiziger.getReiziger_id();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM adres WHERE reiziger_id=?");
        stmt.setInt(1, reiziger_id);
        ResultSet rs = stmt.executeQuery(); // Get results

        //Get first address.
        ArrayList<Adres> adressen = adresRsToList(rs, link);
        if (!(adressen.size() >  0)) return null;

        //Link
        Adres adres1 = adressen.get(0);
        adres1.setReiziger(reiziger);
        reiziger.setAdres(adres1);
        return adres1;
    }

    @Override
    public ArrayList<Adres> findAll(boolean link) throws SQLException {
        Statement stmt = AdresDAOPsql.DAO.connection.createStatement(); // Create statement
        String sql = "SELECT * FROM adres";
        ResultSet rs = stmt.executeQuery(sql); // Get results
        return adresRsToList(rs, link);
    }

    public ArrayList<Adres> adresRsToList(ResultSet rs, boolean link) throws SQLException {
        ArrayList<Adres> adressen = new ArrayList<>();

        while (rs.next()){
            //Link the values to a new object
            Adres adres = new Adres(
                    rs.getInt("adres_id"),
                    rs.getInt("reiziger_id"),
                    rs.getString("postcode"),
                    rs.getString("huisnummer"),
                    rs.getString("straat"),
                    rs.getString("woonplaats")
            );

            if (link){
                //Links adres and reiziger both ways
                Reiziger reiz = ReizigerDAOPsql.DAO.findById(adres.getReiziger_id(), false);
                reiz.setAdres(adres);
                adres.setReiziger(reiz);

                //Get OVKaarten and link both ways
                OVChipkaartDAOPsql.DAO.findByReiziger(reiz, false);
            }

            //Add to list
            adressen.add(adres);
        }

        return adressen;
    }
}
