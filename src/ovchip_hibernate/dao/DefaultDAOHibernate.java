package ovchip_hibernate.dao;

import ovchip_dao.domein.OVChipKaart;

import static ovchip_hibernate.HibernateService.*;

abstract class DefaultDAOHibernate {

    public void save(Object object) {
        openTransaction();
        getCurrentSession().persist(object);
        commitTransaction();
    }

    public void update(Object object) {
        openTransaction();
        getCurrentSession().update(object);
        commitTransaction();
    }

    public void delete(Object object) {
        openTransaction();
        getCurrentSession().delete(object);
        commitTransaction();
    }
}