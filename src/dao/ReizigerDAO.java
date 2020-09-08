package dao;

import domein.Reiziger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ReizigerDAO {
    public boolean save(Reiziger reiziger) throws Exception;
    public boolean update(Reiziger reiziger) throws SQLException;
    public boolean delete(Reiziger reiziger) throws SQLException;
    public Reiziger findById(int id) throws SQLException;
    public List<Reiziger> findByGb(String datum) throws SQLException;
    public List<Reiziger> findAll(boolean singleRecurse) throws SQLException;
}