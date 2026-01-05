package com.example.classes;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import java.util.Date;

@Entity
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date dateCommande;

    public Commande() {}
    public Commande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public int getId() { return id; }
    public Date getDateCommande() { return dateCommande; }
    public void setDateCommande(Date dateCommande) { this.dateCommande = dateCommande; }
}
