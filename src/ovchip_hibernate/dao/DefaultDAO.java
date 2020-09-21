package ovchip_hibernate.dao;

import ovchip_dao.domein.OVChipKaart;

import java.sql.SQLException;

public interface DefaultDAO {
    public void save(Object object);
    public void update(Object object);
    public void delete(Object object);
}
