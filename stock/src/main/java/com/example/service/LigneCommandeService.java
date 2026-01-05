package com.example.service;

import com.example.classes.LigneCommandeProduit;
import com.example.dao.IDao;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class LigneCommandeService implements IDao<LigneCommandeProduit> {

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    private Session getSession() {
        return sessionFactory.openSession(); // openSession() pour main simple
    }

    @Override
    public void create(LigneCommandeProduit o) {
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        session.save(o);
        tx.commit();
        session.close();
    }

    @Override
    public void update(LigneCommandeProduit o) {
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        session.update(o);
        tx.commit();
        session.close();
    }

    @Override
    public void delete(LigneCommandeProduit o) {
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        session.delete(o);
        tx.commit();
        session.close();
    }

    @Override
    public LigneCommandeProduit findById(int id) {
        Session session = getSession();
        LigneCommandeProduit obj = session.get(LigneCommandeProduit.class, id);
        session.close();
        return obj;
    }

    @Override
    public List<LigneCommandeProduit> findAll() {
        Session session = getSession();
        List<LigneCommandeProduit> list = session.createQuery("from LigneCommandeProduit", LigneCommandeProduit.class).list();
        session.close();
        return list;
    }
    public List<LigneCommandeProduit> findByCommande(int commandeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from LigneCommandeProduit l where l.commande.id = :cid", LigneCommandeProduit.class)
                    .setParameter("cid", commandeId)
                    .list();
        }
    }

}
