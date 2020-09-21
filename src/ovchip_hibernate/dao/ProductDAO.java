package ovchip_hibernate.dao;

import ovchip_hibernate.domein.OVChipKaart;
import ovchip_hibernate.domein.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO extends DefaultDAO {
    List<Product> findByOV(OVChipKaart ovChipKaart) throws SQLException;
    public List<Product> findAll(boolean link) throws SQLException;
}