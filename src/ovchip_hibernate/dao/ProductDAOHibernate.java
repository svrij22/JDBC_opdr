package ovchip_hibernate.dao;

import org.hibernate.query.Query;
import ovchip_hibernate.domein.Product;
import ovchip_hibernate.domein.OVChipKaart;
import ovchip_hibernate.HibernateService;

import java.util.List;

import static ovchip_hibernate.HibernateService.*;

public class ProductDAOHibernate extends DefaultDAOHibernate implements ProductDAO {

    @Override
    public List<Product> findByOV(OVChipKaart ovChipKaart) {
        Query<Product> query = getCurrentSession().createQuery(
                "from Product p join p.ovChipKaarten ovc where ovc.kaart_nummer=?1");
        query.setParameter(1, ovChipKaart.getKaartnummer());
        return query.list();
    }

    @Override
    public List<Product> findAll(boolean link) {
        Query<Product> query = HibernateService.getCurrentSession().createQuery("from Product");
        return query.list();
    }
}
