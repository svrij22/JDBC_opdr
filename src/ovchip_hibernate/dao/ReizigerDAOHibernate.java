package ovchip_hibernate.dao;

import org.hibernate.query.Query;
import ovchip_hibernate.HibernateService;
import ovchip_hibernate.domein.Reiziger;

import java.sql.Date;
import java.util.List;

public class ReizigerDAOHibernate extends DefaultDAOHibernate implements ReizigerDAO {

    @Override
    public Reiziger findById(int id) {
        Query<Reiziger> query = HibernateService.getCurrentSession().createQuery("from Reiziger where reiziger_id=?1");
        query.setParameter(1, id);
        return query.list().get(0);
    }

    @Override
    public Reiziger findByGb(String datum) {
        Query<Reiziger> query = HibernateService.getCurrentSession().createQuery("from Reiziger where geboortedatum=?1");
        query.setParameter(1, Date.valueOf(datum));
        return (Reiziger) query.list().get(0);
    }

    @Override
    public List<Reiziger> findAll() {
        Query<Reiziger> query = HibernateService.getCurrentSession().createQuery("from Reiziger");
        return query.list();
    }
}
