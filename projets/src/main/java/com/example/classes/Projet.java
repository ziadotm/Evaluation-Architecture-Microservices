package com.example.classes;


import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Temporal(TemporalType.DATE)
    private Date dateFin;

    @ManyToOne
    private Employe chefProjet;

    @OneToMany(mappedBy = "projet")
    private Set<Tache> taches;

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public Employe getChefProjet() { return chefProjet; }
    public void setChefProjet(Employe chefProjet) { this.chefProjet = chefProjet; }
    public Set<Tache> getTaches() { return taches; }
    public void setTaches(Set<Tache> taches) { this.taches = taches; }
}
