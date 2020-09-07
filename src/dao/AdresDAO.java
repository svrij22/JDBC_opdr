package dao;

import domein.Adres;
import domein.Reiziger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface AdresDAO {
    public boolean save(Adres adres) throws Exception;
    public boolean update(Adres adres) throws SQLException;
    public boolean delete(Adres adres) throws SQLException;

    public static List<Adres> findByReiziger(Reiziger reiziger) throws SQLException {
        return null;
    }

    public static ArrayList<Adres> findAll() throws SQLException {
        return null;
    }
}
