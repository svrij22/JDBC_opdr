package ovchip_hibernate.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ovchip_hibernate.domein.Reiziger;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class ReizigerDAOHibernate implements ReizigerDAO{

    private Session session;

    public ReizigerDAOHibernate(Session session) {
        this.session = session;
    }

    @Override
    public void save(Reiziger reiziger) throws Exception {
        session.save(reiziger);
    }

    @Override
    public void update(Reiziger reiziger) throws SQLException {
        session.update(reiziger);
    }

    @Override
    public void delete(Reiziger reiziger) throws SQLException {
        session.delete(reiziger);
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        Query<Reiziger> query = session.createQuery("from Reiziger where reiziger_id=?1");
        query.setParameter(1, id);
        return query.list().get(0);
    }

    @Override
    public Reiziger findByGb(String datum) throws SQLException {
        Query<Reiziger> query = session.createQuery("from Reiziger where geboortedatum=?1");
        query.setParameter(1, Date.valueOf(datum));
        return (Reiziger)query.list().get(0);
    }

    @Override
    public List<Reiziger> findAll() {
        Query<Reiziger> query = session.createQuery("from Reiziger");
        return query.list();
    }
}
