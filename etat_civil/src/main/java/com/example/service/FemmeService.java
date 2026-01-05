package com.example.service;


import com.example.beans.Femme;
import com.example.dao.GenericDao;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

public class FemmeService extends GenericDao<Femme> {
    public FemmeService() { super(Femme.class); }

    // Méthode native : nombre d'enfants d'une femme entre deux dates
    public Integer countChildrenBetween(Long femmeId, Date d1, Date d2) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Object res = session.createNativeQuery(
                        "SELECT COALESCE(SUM(nb_enfant),0) FROM mariage WHERE femme_id = :fid AND date_debut >= :d1 AND date_debut <= :d2")
                .setParameter("fid", femmeId)
                .setParameter("d1", d1)
                .setParameter("d2", d2)
                .uniqueResult();
        session.close();
        if (res == null) return 0;
        Number n = (Number) res;
        return n.intValue();
    }

    // Méthode nommée (NamedQuery) : femmes mariées au moins deux fois
    public List<Femme> findMarriedAtLeastTwice() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        TypedQuery<Femme> q = session.getNamedQuery("Femme.findMarriedAtLeastTwice");
        List<Femme> res = q.getResultList();
        session.close();
        return res;
    }

    // Méthode utilisant Criteria API pour afficher les hommes mariés à 4 femmes entre deux dates
    // Retour : List<Object[]> où Object[0]=Homme, Object[1]=Long countDistinctFemmes
    public List<Object[]> hommesMarriedToFourWomenBetween(Date d1, Date d2) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        // Root sur Mariage
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<?> mariage = cq.from(com.example.beans.Mariage.class);

        // join vers homme et femme
        Path<?> hommePath = mariage.get("homme");
        Path<?> femmePath = mariage.get("femme");

        // sélection: homme, count distinct femme
        cq.multiselect(hommePath, cb.countDistinct(femmePath));
        Predicate p1 = cb.greaterThanOrEqualTo(mariage.get("dateDebut").as(Date.class), d1);
        Predicate p2 = cb.lessThanOrEqualTo(mariage.get("dateDebut").as(Date.class), d2);
        cq.where(cb.and(p1, p2));
        cq.groupBy(hommePath);
        cq.having(cb.greaterThanOrEqualTo(cb.countDistinct(femmePath), 4L));

        List<Object[]> result = session.createQuery(cq).getResultList();
        session.close();
        return result;
    }
}

