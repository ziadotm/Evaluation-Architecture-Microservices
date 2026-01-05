package com.example.test;

import com.example.classes.Categorie;
import com.example.classes.Commande;
import com.example.classes.Produit;
import com.example.classes.LigneCommandeProduit;
import com.example.service.ProduitService;
import com.example.service.CommandeService;
import com.example.service.CategorieService;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Services
        CategorieService categorieService = new CategorieService();
        ProduitService produitService = new ProduitService();
        CommandeService commandeService = new CommandeService();

        // 1️⃣ Création de catégories
        Categorie cat1 = new Categorie("INF", "Informatique");
        Categorie cat2 = new Categorie("ACC", "Accessoires");
        categorieService.create(cat1);
        categorieService.create(cat2);

        // 2️⃣ Création de produits
        Produit p1 = new Produit("ES12", 120, cat1);
        Produit p2 = new Produit("ZR85", 100, cat1);
        Produit p3 = new Produit("EE85", 200, cat2);
        produitService.create(p1);
        produitService.create(p2);
        produitService.create(p3);

        // 3️⃣ Création d'une commande
        Commande cmd = new Commande();
        cmd.setDateCommande(new Date()); // date actuelle
        commandeService.create(cmd);

        // 4️⃣ Ajout de lignes de commande
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        LigneCommandeProduit l1 = new LigneCommandeProduit(p1, cmd, 7);
        LigneCommandeProduit l2 = new LigneCommandeProduit(p2, cmd, 14);
        LigneCommandeProduit l3 = new LigneCommandeProduit(p3, cmd, 5);

        session.persist(l1);
        session.persist(l2);
        session.persist(l3);
        tx.commit();
        session.close();

        // 5️⃣ Affichage de la commande
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        System.out.println("Commande : " + cmd.getId() + "     Date : " + sdf.format(cmd.getDateCommande()));
        System.out.println("Liste des produits :");
        System.out.println("Référence   Prix    Quantité");

        List<Object[]> produits = produitService.findProduitsByCommande(cmd);
        for (Object[] ligne : produits) {
            System.out.println(ligne[0] + "        " + ligne[1] + " DH  " + ligne[2]);
        }

        // 6️⃣ Produits avec prix > 100 DH
        System.out.println("\nProduits avec prix > 100 DH :");
        List<Produit> produitsPrixSup100 = produitService.findByPrixSup100();
        for (Produit p : produitsPrixSup100) {
            System.out.println(p.getReference() + " - " + p.getPrix() + " DH");
        }

        // Fermer SessionFactory
        HibernateUtil.getSessionFactory().close();
    }
}
