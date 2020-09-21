package ovchip_dao.dao;

import ovchip_dao.domein.OVChipKaart;
import ovchip_dao.domein.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {
    public boolean save(OVChipKaart ovchip) throws Exception;
    public boolean update(OVChipKaart ovchip) throws SQLException;
    public boolean delete(OVChipKaart ovchip) throws SQLException;
    public OVChipKaart findByNummer(int id) throws SQLException;
    public List<OVChipKaart> findByReiziger(Reiziger reiziger, boolean link) throws SQLException;
    public List<OVChipKaart> findAll(boolean link) throws SQLException;
}