package dao;

import domein.OVChipKaart;
import domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class OVChipkaartDAOPsql implements OVChipkaartDAO{
    Connection connection;

    public OVChipkaartDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(OVChipKaart ovchip) throws Exception {
        //Already exists
        if (this.nummerExists(ovchip.getKaartnummer())){
            throw new Exception("Reiziger met deze ID bestaat al.");
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
        boolean exists = false;
        try {
            exists = (rs.getInt("kaart_nummer") == kaartnummer);
        }catch (Exception ignored){}
        return exists;
    }

    @Override
    public boolean update(OVChipKaart ovchip) throws SQLException {
        //Create statement
        PreparedStatement stmt = connection.prepareStatement("UPDATE ov_chipkaart SET reiziger_id=?, geldig_tot=?, " +
                "klasse=?, saldo=? WHERE kaart_nummer=?");

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
    public List<OVChipKaart> findByReiziger(Reiziger reiziger) throws SQLException {
        ArrayList<OVChipKaart> ovkaarten = new ArrayList<>();
        for (OVChipKaart ov : this.findAll()) {
            if (reiziger.getReiziger_id() == ov.getReiziger_id()) ovkaarten.add(ov);
        }
        return ovkaarten;
    }

    @Override
    public List<OVChipKaart> findAll() throws SQLException {
        ArrayList<OVChipKaart> ovChipKaarten = new ArrayList<>();
        Statement stmt = connection.createStatement(); // Create statement
        ResultSet rs = stmt.executeQuery("SELECT * FROM ov_chipkaart"); // Get results

        while (rs.next()){

            //Get alle key and value pairs from database
            String[] keys = new String[]{"kaart_nummer", "geldig_tot", "klasse", "saldo", "reiziger_id"};

            ArrayList<String> vals = new ArrayList<>();
            for (String key : keys) vals.add(rs.getString(key));

            //Link the values to a new object
            OVChipKaart chipkaart = new OVChipKaart(
                    parseInt(vals.get(0)),
                    parseInt(vals.get(2)),
                    parseInt(vals.get(4)),
                    Date.valueOf(vals.get(1)),
                    Double.parseDouble(vals.get(3))
            );

            //Set reiziger
            Reiziger reiziger = ReizigerDAOPsql.DAO.findById(chipkaart.getReiziger_id(), true);
            reiziger.addKaart(chipkaart);
            chipkaart.setReiziger(reiziger);

            //Add to list
            ovChipKaarten.add(chipkaart);
        }

        return ovChipKaarten;
    }
}
