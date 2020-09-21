package ovchip_hibernate.dao;

import org.hibernate.query.Query;
import ovchip_hibernate.domein.OVChipKaart;
import ovchip_hibernate.domein.Reiziger;

import java.sql.SQLException;
import java.util.List;

import static ovchip_hibernate.HibernateService.*;

public class OVChipkaartDAOHibernate extends DefaultDAOHibernate implements OVChipkaartDAO {

    @Override
    public OVChipKaart findByNummer(int id) {
        Query<OVChipKaart> query = getCurrentSession().createQuery("FROM OVChipKaart WHERE kaart_nummer=?1");
        query.setParameter(1, id);
        return query.list().get(0);
    }

    @Override
    public List<OVChipKaart> findByReiziger(Reiziger reiziger)  {
        Query<OVChipKaart> query = getCurrentSession().createQuery("FROM OVChipKaart WHERE reiziger_id=?1");
        query.setParameter(1, reiziger.getReiziger_id());
        return query.list();
    }

    @Override
    public List<OVChipKaart> findAll() {
        Query<OVChipKaart> query = getCurrentSession().createQuery("from OVChipKaart");
        return query.list();
    }
}
