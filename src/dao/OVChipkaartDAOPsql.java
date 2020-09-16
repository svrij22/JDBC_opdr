package dao;

import domein.Adres;
import domein.OVChipKaart;
import domein.Product;
import domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
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

    public List<OVChipKaart> findByProd(int productNum, boolean link) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("select * from ov_chipkaart o join ov_chipkaart_product ocp on o.kaart_nummer = ocp.kaart_nummer where product_nummer=?");
        stmt.setInt(1, productNum);
        ResultSet rs = stmt.executeQuery(); // Get results
        return ovRsToList(rs, link);
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

        //Link or .. re-link really..
        reiziger.setKaarten(kaarten);
        for (OVChipKaart kaart : kaarten )
            kaart.setReiziger(reiziger);

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
        HashSet<Product> products = new HashSet<>();

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
                AdresDAOPsql.DAO.findByReiziger(reiziger, false);

                //Link all products both ways
                //HashSet only allows 1 unique prods
                List<Product> matchingProds = ProductDAOPsql.DAO.findByOV(chipkaart, false);
                products.addAll(matchingProds);
                if (products.size() > 0){
                    for (Product prod:products) {
                        if (matchingProds.contains(prod)) {
                            prod.addOVKaart(chipkaart);
                            chipkaart.addProduct(prod);
                        }
                    }
                }
            }

            //Add to list
            ovChipKaarten.add(chipkaart);
        }

        return ovChipKaarten;
    }
}
