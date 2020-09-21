package ovchip_dao.dao;

import ovchip_dao.domein.OVChipKaart;
import ovchip_dao.domein.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    public boolean save(Product prod) throws Exception;
    public boolean update(Product prod) throws SQLException;
    public boolean delete(Product prod) throws SQLException;
    public List<Product> findByOV(OVChipKaart ovChipKaart, boolean link) throws SQLException;
    public List<Product> findAll(boolean link) throws SQLException;
}