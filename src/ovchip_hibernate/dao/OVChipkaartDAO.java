package ovchip_hibernate.dao;

import ovchip_dao.domein.OVChipKaart;
import ovchip_dao.domein.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO extends DefaultDAO {
    public OVChipKaart findByNummer(int id) throws SQLException;
    public List<OVChipKaart> findByReiziger(Reiziger reiziger, boolean link) throws SQLException;
    public List<OVChipKaart> findAll(boolean link) throws SQLException;
}