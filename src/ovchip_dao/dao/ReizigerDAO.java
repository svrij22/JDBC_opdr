package ovchip_dao.dao;

import ovchip_dao.domein.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface ReizigerDAO {
    public boolean save(Reiziger reiziger) throws Exception;
    public boolean update(Reiziger reiziger) throws SQLException;
    public boolean delete(Reiziger reiziger) throws SQLException;
    public Reiziger findById(int id, boolean link) throws SQLException;
    public List<Reiziger> findByGb(String datum, boolean link) throws SQLException;
    public List<Reiziger> findAll(boolean link) throws SQLException;
}