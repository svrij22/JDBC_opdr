package ovchip_hibernate.dao;

import ovchip_hibernate.domein.OVChipKaart;
import ovchip_hibernate.domein.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO extends DefaultDAOHibernate.DefaultDAO {
    public OVChipKaart findByNummer(int id) ;
    public List<OVChipKaart> findByReiziger(Reiziger reiziger) ;
    public List<OVChipKaart> findAll() ;
}