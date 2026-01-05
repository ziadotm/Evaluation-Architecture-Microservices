package com.example.service;

import com.example.classes.EmployeTache;
import com.example.dao.IDao;
import com.example.classes.Projet;
import com.example.classes.Tache;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;

public class ProjetService implements IDao<Projet> {

    @Override
    public boolean create(Projet p) {
        Transaction t = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            t = session.beginTransaction();
            session.persist(p);
            t.commit();
            return true;
        } catch (Exception ex) {
            if (t != null) t.rollback();
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Projet p) {
        Transaction t = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            t = session.beginTransaction();
            session.merge(p);
            t.commit();
            return true;
        } catch (Exception ex) {
            if (t != null) t.rollback();
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Projet p) {
        Transaction t = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            t = session.beginTransaction();
            session.remove(p);
            t.commit();
            return true;
        } catch (Exception ex) {
            if (t != null) t.rollback();
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Projet findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Projet.class, id);
        }
    }

    @Override
    public List<Projet> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Projet", Projet.class).list();
        }
    }

    // Méthodes spécifiques
    public List<Tache> getTachesPlanifiees(int projetId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from com.example.classes.Tache t where t.projet.id=:id", Tache.class)
                    .setParameter("id", projetId)
                    .list();
        }
    }

    public List<Tache> getTachesRealisees(int projetId, java.util.Date debut, java.util.Date fin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from com.example.classes.Tache t where t.projet.id=:id and t.dateDebut>=:debut and t.dateFin<=:fin", Tache.class)
                    .setParameter("id", projetId)
                    .setParameter("debut", debut)
                    .setParameter("fin", fin)
                    .list();
        }
    }

    // Méthode corrigée pour afficher les tâches réalisées avec les dates réelles
    public void afficherTachesRealiseesAvecDates(int projetId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Projet projet = session.get(Projet.class, projetId);

        if (projet == null) {
            System.out.println("Projet introuvable !");
            return;
        }

        // Formater la date de début du projet
        SimpleDateFormat sdfProjet = new SimpleDateFormat("dd MMMM yyyy");
        String dateDebutProjet = sdfProjet.format(projet.getDateDebut());

        System.out.println("Projet : " + projet.getId() + "  Nom : " + projet.getNom() + "  Date début : " + dateDebutProjet);

        System.out.println("Liste des tâches:");
        System.out.printf("%-4s %-15s %-15s %-15s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");

        int num = 1;
        SimpleDateFormat sdfTache = new SimpleDateFormat("dd/MM/yyyy");

        for (Tache tache : projet.getTaches()) {
            if (!tache.getEmployeTaches().isEmpty()) {
                for (EmployeTache et : tache.getEmployeTaches()) {
                    String dateDebutReelle = sdfTache.format(et.getDateDebutReelle());
                    String dateFinReelle = sdfTache.format(et.getDateFinReelle());
                    System.out.printf("%-4d %-15s %-15s %-15s%n", num, tache.getNom(), dateDebutReelle, dateFinReelle);
                    num++;
                }
            }
        }

        session.close();
    }
}
