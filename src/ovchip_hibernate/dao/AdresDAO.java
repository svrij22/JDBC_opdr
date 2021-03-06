package ovchip_hibernate.dao;

import ovchip_dao.domein.Adres;
import ovchip_dao.domein.Reiziger;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AdresDAO {
    public boolean save(Adres adres) throws Exception;
    public boolean update(Adres adres) throws SQLException;
    public boolean delete(Adres adres) throws SQLException;

    public Adres findById(int id, boolean link) throws SQLException;
    public Adres findByReiziger(Reiziger reiziger, boolean link) throws SQLException;
    public ArrayList<Adres> findAll(boolean link) throws SQLException;
}
