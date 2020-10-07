package ovchip_hibernate.dao;

import ovchip_hibernate.domein.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface ReizigerDAO {
    public void save(Reiziger reiziger) throws Exception;
    public void update(Reiziger reiziger) throws SQLException;
    public void delete(Reiziger reiziger) throws SQLException;
    Reiziger findById(int id) throws SQLException;
    Reiziger findByGb(String datum) throws SQLException;
    List<Reiziger> findAll() throws SQLException;
}