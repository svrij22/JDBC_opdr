package dao;

import domein.Adres;
import domein.OVChipKaart;
import domein.Product;
import domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class OVChipkaartDAOPsql implements OVChipkaartDAO{
    Connection connection;
    public static OVChipkaartDAOPsql DAO;

    public OVChipkaartDAOPsql(Connection connection) {
        this.connection = connection;
        DAO = this;
    }

    @Override
    public boolean save(OVChipKaart ovchip) throws Exception {
        //Already exists
        if (this.nummerExists(ovchip.getKaartnummer())){
            throw new Exception("OVCHIP met deze ID bestaat al.");
        }

        //Create statement
        PreparedStatement stmt = connection.prepareStatement("insert into ov_chipkaart values (?, ?, ?, ?, ?)"); // Create statement
        stmt.setInt(1, ovchip.getKaartnummer());
        stmt.setDate(2, (Date) ovchip.getGeldig_tot());
        stmt.setInt(3, ovchip.getKlasse());
        stmt.setDouble(4, ovchip.getSaldo());
        stmt.setInt(5, ovchip.getReiziger_id());

        //Return result
        return stmt.execute();
    }

    private boolean nummerExists(int kaartnummer) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM ov_chipkaart WHERE kaart_nummer=?"); // Create statement
        stmt.setInt(1, kaartnummer);
        ResultSet rs = stmt.executeQuery(); // Get results

        rs.next();
        try {
            return (rs.getInt("kaart_nummer") == kaartnummer);
        }catch (Exception ignored){}
        return false;
    }

    @Override
    public boolean update(OVChipKaart ovchip) throws SQLException {
        //Create statement
        PreparedStatement stmt = connection.prepareStatement("UPDATE ov_chipkaart SET reiziger_id=?, geldig_tot=?, klasse=?, saldo=? WHERE kaart_nummer=?");

        stmt.setInt(1, ovchip.getReiziger_id());
        stmt.setDate(2, (Date) ovchip.getGeldig_tot());
        stmt.setInt(3, ovchip.getKlasse());
        stmt.setDouble(4, ovchip.getSaldo());
        stmt.setInt(5, ovchip.getKaartnummer());

        //Execute and return value
        return stmt.execute();
    }

    @Override
    public boolean delete(OVChipKaart ovchip) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM ov_chipkaart WHERE kaart_nummer=?");
        stmt.setInt(1, ovchip.getKaartnummer());
        return stmt.execute();
    }

    @Override
    public OVChipKaart findByNummer(int id) throws SQLException {
        return null;
    }

    @Override
    public List<OVChipKaart> findByReiziger(Reiziger reiziger, boolean link) throws SQLException {
        //Prepare statement
        int reiziger_id = reiziger.getReiziger_id();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM ov_chipkaart WHERE reiziger_id=?");
        stmt.setInt(1, reiziger_id);

        //Get address list
        ResultSet rs = stmt.executeQuery(); // Get results
        List<OVChipKaart> kaarten = ovRsToList(rs, link);
        reiziger.setKaarten(kaarten);
        return kaarten;
    }

    @Override
    public List<OVChipKaart> findAll(boolean link) throws SQLException {
        Statement stmt = connection.createStatement(); // Create statement
        String sql = "SELECT * FROM ov_chipkaart";
        ResultSet rs = stmt.executeQuery(sql); // Get results
        return ovRsToList(rs, link);
    }

    public List<OVChipKaart> ovRsToList(ResultSet rs, boolean link) throws SQLException {
        ArrayList<OVChipKaart> ovChipKaarten = new ArrayList<>();

        while (rs.next()){
            //Link the values to a new object
            OVChipKaart chipkaart = new OVChipKaart(
                    rs.getInt("kaart_nummer"),
                    rs.getInt("klasse"),
                    rs.getInt("reiziger_id"),
                    rs.getDate("geldig_tot"),
                    rs.getDouble("saldo")
            );

            //Links the reiziger, adres and OV
            if (link){

                //Link kaart both ways
                Reiziger reiziger = ReizigerDAOPsql.DAO.findById(chipkaart.getReiziger_id(), false);
                reiziger.addKaart(chipkaart);
                chipkaart.setReiziger(reiziger);

                //Get adress and link both ways
                Adres adres = AdresDAOPsql.DAO.findByReiziger(reiziger, false);
                if (adres != null) {
                    adres.setReiziger(reiziger);
                    reiziger.setAdres(adres);
                }

                //Link all products both ways
                List<Product> products = ProductDAOPsql.DAO.findByOV(chipkaart, false);
                if (products.size() > 0){
                    chipkaart.setProducten((ArrayList<Product>) products);
                    for (Product prod:products) prod.addOVKaart(chipkaart);
                }
            }

            //Add to list
            ovChipKaarten.add(chipkaart);
        }

        return ovChipKaarten;
    }
}
