package ovchip_hibernate.dao;

import org.hibernate.query.Query;
import ovchip_hibernate.domein.Adres;
import ovchip_hibernate.domein.Reiziger;
import ovchip_hibernate.HibernateService;

import java.util.List;

import static ovchip_hibernate.HibernateService.getCurrentSession;

public class AdresDAOHibernate extends DefaultDAOHibernate implements AdresDAO{

    @Override
    public Adres findById(int id) {
        Query<Adres> query = HibernateService.getCurrentSession().createQuery("from Adres where adres_id=?1");
        query.setParameter(1, id);
        return query.list().get(0);
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        Query<Adres> query = getCurrentSession().createQuery("FROM Adres WHERE reiziger_id=?1");
        query.setParameter(1, reiziger.getReiziger_id());
        return query.list().get(0);
    }

    @Override
    public List<Adres> findAll() {
        Query<Adres> query = getCurrentSession().createQuery("from Adres");
        return query.list();
    }
}
