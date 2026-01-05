package com.example.service;

import com.example.classes.Categorie;
import com.example.classes.Commande;
import com.example.classes.Produit;
import com.example.dao.IDao;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;

public class ProduitService implements IDao<Produit> {

    @Override
    public void create(Produit o) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(o);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Produit o) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(o);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Produit o) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.delete(o);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Produit findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Produit.class, id);
        }
    }



    @Override
    public List<Produit> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Produit", Produit.class).list();
        }
    }

    // ✅ Produits par catégorie
    public List<Produit> findByCategorie(Categorie cat) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Produit p WHERE p.categorie = :cat", Produit.class)
                    .setParameter("cat", cat)
                    .list();
        }
    }

    // ✅ Produits commandés entre deux dates
    public List<Object[]> findProduitsEntreDates(Date d1, Date d2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT p.reference, p.prix, l.quantite " +
                                    "FROM LigneCommandeProduit l JOIN l.produit p JOIN l.commande c " +
                                    "WHERE c.dateCommande BETWEEN :d1 AND :d2",
                            Object[].class)
                    .setParameter("d1", d1)
                    .setParameter("d2", d2)
                    .list();
        }
    }

    // ✅ Produits d'une commande donnée
    public List<Object[]> findProduitsByCommande(Commande cmd) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT p.reference, p.prix, l.quantite " +
                                    "FROM LigneCommandeProduit l JOIN l.produit p " +
                                    "WHERE l.commande = :cmd", Object[].class)
                    .setParameter("cmd", cmd)
                    .list();
        }
    }

    // ✅ Produits avec prix > 100 (requête nommée)
    public List<Produit> findByPrixSup100() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Produit.findByPrixSup100", Produit.class)
                    .list();
        }
    }
}
