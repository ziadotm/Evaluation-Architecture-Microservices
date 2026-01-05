package com.example;


import com.example.beans.*;
import com.example.service.*;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        HommeService hommeService = new HommeService();
        FemmeService femmeService = new FemmeService();
        MariageService mariageService = new MariageService();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        // 1) Créer 10 femmes
        List<Femme> femmes = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Femme f = new Femme();
            f.setNom("FEMME" + i);
            f.setPrenom("PrenomF" + i);
            // dates de naissance variées (plus petit = plus âgée)
            f.setDateNaissance(df.parse((i < 10 ? "0" + i : i) + "/01/197" + (i % 10)));
            f.setAdresse("Adresse " + i);
            f.setTelephone("0600000" + i);
            femmeService.save(f);
            femmes.add(f);
        }

        // 2) Créer 5 hommes
        List<Homme> hommes = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Homme h = new Homme();
            h.setNom("HOMME" + i);
            h.setPrenom("PrenomH" + i);
            h.setDateNaissance(df.parse((i < 10 ? "0" + i : i) + "/02/197" + (i % 10)));
            h.setAdresse("AdresseH " + i);
            h.setTelephone("0611111" + i);
            hommeService.save(h);
            hommes.add(h);
        }

        // 3) Créer mariages (quelques en cours, quelques terminés)
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        // HOMME1: 3 mariages en cours, 1 terminé
        Homme h1 = hommes.get(0);
        createMariage(session, h1, femmes.get(0), df.parse("03/09/1990"), null, 4);
        createMariage(session, h1, femmes.get(1), df.parse("03/09/1995"), null, 2);
        createMariage(session, h1, femmes.get(2), df.parse("04/11/2000"), null, 3);
        createMariage(session, h1, femmes.get(3), df.parse("03/09/1989"), df.parse("03/09/1990"), 0);

        // HOMME2
        createMariage(session, hommes.get(1), femmes.get(4), df.parse("01/01/1988"), df.parse("01/01/1995"), 1);
        createMariage(session, hommes.get(1), femmes.get(5), df.parse("02/02/1996"), null, 2);

        // HOMME3: marié à 4 femmes entre 1990 et 2010
        createMariage(session, hommes.get(2), femmes.get(6), df.parse("05/05/1991"), df.parse("05/05/1993"), 1);
        createMariage(session, hommes.get(2), femmes.get(7), df.parse("06/06/1994"), df.parse("06/06/1996"), 0);
        createMariage(session, hommes.get(2), femmes.get(8), df.parse("07/07/1997"), df.parse("07/07/1999"), 2);
        createMariage(session, hommes.get(2), femmes.get(9), df.parse("08/08/2000"), null, 1);

        tx.commit();
        session.close();

        // ---- Affichages demandés ----

        // Liste des femmes
        System.out.println("\n--- Liste des femmes ---");
        for (Femme f : femmeService.findAll()) {
            System.out.println("ID: " + f.getId() + "    Nom: " + f.getNom() + " " + f.getPrenom());
        }

        // Femme la plus âgée (dateNaissance min)
        Femme oldest = femmeService.findAll().stream()
                .min(Comparator.comparing(Femme::getDateNaissance))
                .orElse(null);
        System.out.println("\nFemme la plus âgée: " + (oldest != null ? oldest.getNom() + " " + oldest.getPrenom() : "aucune"));

        // Épouses d'un homme donné (HOMME1) entre deux dates
        System.out.println("\nÉpouses de " + h1.getNom() + " entre 01/01/1989 et 31/12/2001 :");
        Date d1 = df.parse("01/01/1989");
        Date d2 = df.parse("31/12/2001");
        List<Mariage> epouses = (new HommeService()).getEpousesBetween(h1.getId(), d1, d2);
        for (Mariage m : epouses) {
            System.out.println("Femme: " + m.getFemme().getNom() + " DateDébut: " + df.format(m.getDateDebut()));
        }

        // Nombre d'enfants d'une femme (femmes.get(0)) entre deux dates
        System.out.println("\nNombre d'enfants de " + femmes.get(0).getNom() + " entre 01/01/1989 et 31/12/2001 :");
        Integer nb = femmeService.countChildrenBetween(femmes.get(0).getId(), d1, d2);
        System.out.println(nb);

        // Femmes mariées deux fois ou plus
        System.out.println("\nFemmes mariées au moins deux fois :");
        for (Femme f : femmeService.findMarriedAtLeastTwice()) {
            System.out.println(f.getNom() + " " + f.getPrenom());
        }

        // Hommes mariés à >= 4 femmes entre deux dates
        System.out.println("\nHommes mariés à >=4 femmes entre 01/01/1990 et 31/12/2010 :");
        Date d3 = df.parse("01/01/1990");
        Date d4 = df.parse("31/12/2010");
        List<Object[]> list = femmeService.hommesMarriedToFourWomenBetween(d3, d4);
        for (Object[] row : list) {
            Homme hh = (Homme) row[0];
            System.out.println(hh.getNom() + " - nombre distinct femmes: " + row[1]);
        }

        // Mariages d'un homme (HOMME1) avec tous les détails (en cours / échoués)
        System.out.println("\nMariages de " + h1.getNom() + " :");
        List<Mariage> all = (new HommeService()).getMariagesDetails(h1.getId());
        System.out.println("\nNom : " + h1.getNom() + " " + h1.getPrenom());
        System.out.println("Mariages En Cours :");
        int i = 1;
        for (Mariage m : all) {
            if (m.getDateFin() == null) {
                System.out.println(i + ". Femme : " + m.getFemme().getNom() +
                        "   Date Début : " + df.format(m.getDateDebut()) +
                        "    Nbr Enfants : " + m.getNbEnfant());
                i++;
            }
        }
        System.out.println("\nMariages échoués :");
        i = 1;
        for (Mariage m : all) {
            if (m.getDateFin() != null) {
                System.out.println(i + ". Femme : " + m.getFemme().getNom() +
                        "  Date Début : " + df.format(m.getDateDebut()) +
                        "\nDate Fin : " + df.format(m.getDateFin()) +
                        "    Nbr Enfants : " + m.getNbEnfant());
                i++;
            }
        }

        // fermer la SessionFactory
        HibernateUtil.getSessionFactory().close();
    }

    private static void createMariage(org.hibernate.Session session, Homme h, Femme f, Date debut, Date fin, Integer nb) {
        Mariage m = new Mariage();
        m.setHomme(h);
        m.setFemme(f);
        m.setDateDebut(debut);
        m.setDateFin(fin);
        m.setNbEnfant(nb);
        session.save(m);
    }
}

