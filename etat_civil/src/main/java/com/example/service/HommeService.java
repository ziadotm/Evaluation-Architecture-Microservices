package com.example.service;


import com.example.beans.Homme;
import com.example.beans.Mariage;
import com.example.dao.GenericDao;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class HommeService extends GenericDao<Homme> {
    public HommeService() { super(Homme.class); }

    // Afficher les épouses (objets Mariage avec la femme joinée) d'un homme entre deux dates
    public List<Mariage> getEpousesBetween(Long hommeId, Date d1, Date d2) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "FROM Mariage m JOIN FETCH m.femme WHERE m.homme.id = :hid AND m.dateDebut >= :d1 AND m.dateDebut <= :d2";
        Query<Mariage> q = session.createQuery(hql, Mariage.class);
        q.setParameter("hid", hommeId);
        q.setParameter("d1", d1);
        q.setParameter("d2", d2);
        List<Mariage> res = q.list();
        session.close();
        return res;
    }

    // Afficher les mariages d'un homme avec détails (jointure femme)
    public List<Mariage> getMariagesDetails(Long hommeId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "FROM Mariage m JOIN FETCH m.femme WHERE m.homme.id = :hid ORDER BY m.dateDebut";
        Query<Mariage> q = session.createQuery(hql, Mariage.class);
        q.setParameter("hid", hommeId);
        List<Mariage> res = q.list();
        session.close();
        return res;
    }
}

