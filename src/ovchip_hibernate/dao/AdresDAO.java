package ovchip_hibernate.dao;

import ovchip_hibernate.domein.Adres;
import ovchip_hibernate.domein.Reiziger;

import java.util.List;

public interface AdresDAO extends DefaultDAOHibernate.DefaultDAO {
    Adres findById(int id);
    Adres findByReiziger(Reiziger reiziger);
    List<Adres> findAll();
}
