package ovchip_hibernate.dao;

import ovchip_hibernate.domein.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface ReizigerDAO extends DefaultDAO {
    Reiziger findById(int id) throws SQLException;
    Reiziger findByGb(String datum) throws SQLException;
    List<Reiziger> findAll() throws SQLException;
}