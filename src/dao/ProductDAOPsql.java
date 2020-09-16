package dao;

import domein.Adres;
import domein.OVChipKaart;
import domein.Product;
import domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {
    Connection connection;
    static ProductDAOPsql DAO;

    public ProductDAOPsql(Connection connection) {
        this.connection = connection;
        DAO = this;
    }

    @Override
    public boolean save(Product product) throws Exception {

        //Already exists
        if (this.nummerExists(product.getProduct_nummer())){
            throw new Exception("Reiziger met deze ID bestaat al.");
        }

        //Create statement
        PreparedStatement stmt = connection.prepareStatement("insert into product values (?, ?, ?, ?)"); // Create statement

        stmt.setInt(1, product.getProduct_nummer());
        stmt.setString(2, product.getName());
        stmt.setString(3, product.getBeschrijving());
        stmt.setDouble(4, product.getPrice());

        //Return result
        return stmt.execute();
    }

    @Override
    public boolean update(Product product) throws SQLException {
        //Create statement
        PreparedStatement stmt = connection.prepareStatement("UPDATE product SET naam=?, beschrijving=?, prijs=? WHERE product_nummer=?");

        stmt.setString(1, product.getName());
        stmt.setString(2, product.getBeschrijving());
        stmt.setDouble(3, product.getPrice());
        stmt.setInt(4, product.getProduct_nummer());

        //Execute and return value
        return stmt.execute();
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        //Create statement
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM product WHERE product_nummer=?");
        stmt.setInt(1, product.getProduct_nummer());
        return stmt.execute();
    }

    public boolean nummerExists(int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM product WHERE product_nummer=?"); // Create statement
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery(); // Get results

        rs.next();
        try {
            return (rs.getInt("product_nummer") == id);
        }catch (Exception ignored){}
        return false;
    }

    @Override
    public List<Product> findByOV(OVChipKaart ovChipKaart, boolean link) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement
                ("select * from product where product_nummer in (select product_nummer from ov_chipkaart_product where kaart_nummer=?)");
        stmt.setInt(1, ovChipKaart.getKaartnummer());
        ResultSet rs = stmt.executeQuery(); // Get results
        return productRsToLs(rs, link);
    }


    @Override
    public List<Product> findAll(boolean link) throws SQLException {
        Statement stmt = connection.createStatement(); // Create statement
        String sql = "SELECT * FROM product";
        ResultSet rs = stmt.executeQuery(sql); // Get results
        return productRsToLs(rs, link);
    }

    public List<Product> productRsToLs(ResultSet rs, boolean link) throws SQLException {
        ArrayList<Product> products = new ArrayList<>();
        HashSet<OVChipKaart> ovkaarten = new HashSet<>();

        //While has next product.
        while (rs.next()){

            //Link the values to a new object
            Product prod = new Product(
                    rs.getInt("product_nummer"),
                    rs.getString("naam"),
                    rs.getString("beschrijving"),
                    rs.getDouble("prijs")
            );

            //Link all products both ways
            //HashSet only allows 1 unique prods
            if (link){
                List<OVChipKaart> matchingOVs = OVChipkaartDAOPsql.DAO.findByProd(prod.getProduct_nummer(), true);
                ovkaarten.addAll(matchingOVs);
                for (OVChipKaart ov : matchingOVs)
                    ov.setProducten(new ArrayList<>()); //Reset list

                if (ovkaarten.size() > 0){
                    for (OVChipKaart ov : ovkaarten) {
                        if (matchingOVs.contains(ov)) {
                            prod.addOVKaart(ov);
                            ov.addProduct(prod);
                        }
                    }
                }
            }

            //Add to list
            products.add(prod);
        }

        return products;
    }
}
