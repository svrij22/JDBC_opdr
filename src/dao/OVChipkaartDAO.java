package dao;

import domein.OVChipKaart;
import domein.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {
    public boolean save(OVChipKaart ovchip) throws Exception;
    public boolean update(OVChipKaart ovchip) throws SQLException;
    public boolean delete(OVChipKaart ovchip) throws SQLException;
    public OVChipKaart findByNummer(int id) throws SQLException;
    public List<OVChipKaart> findByReiziger(Reiziger reiziger) throws SQLException;
    public List<OVChipKaart> findAll() throws SQLException;
}