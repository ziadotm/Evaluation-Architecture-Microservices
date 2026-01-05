package com.example.test;

import com.example.classes.LigneCommandeProduit;
import com.example.service.LigneCommandeService;

public class TestApplication {
    public static void main(String[] args) {

        LigneCommandeService service = new LigneCommandeService();

        LigneCommandeProduit ligne = new LigneCommandeProduit();
        // remplis les champs nécessaires ici
        // exemple : ligne.setQuantite(5);

        service.create(ligne);

        System.out.println("LigneCommandeProduit ajoutée avec succès !");
    }
}
