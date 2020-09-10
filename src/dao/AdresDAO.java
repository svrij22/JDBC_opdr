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

    Adres findById(int id, boolean link) throws SQLException;
    Adres findByReiziger(Reiziger reiziger, boolean link) throws SQLException;
    ArrayList<Adres> findAll(boolean link) throws SQLException;
}
