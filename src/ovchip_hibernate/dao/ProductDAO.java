package ovchip_hibernate.dao;

import ovchip_hibernate.domein.OVChipKaart;
import ovchip_hibernate.domein.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO extends DefaultDAOHibernate.DefaultDAO {
    List<Product> findByOV(OVChipKaart ovChipKaart);
    public List<Product> findAll();
}