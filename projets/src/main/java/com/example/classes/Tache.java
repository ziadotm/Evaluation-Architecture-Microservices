package com.example.classes;


import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(name="Tache.prixSup1000", query="SELECT t FROM Tache t WHERE t.prix > 1000")
})
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Temporal(TemporalType.DATE)
    private Date dateFin;

    private double prix;

    @ManyToOne
    private Projet projet;

    @OneToMany(mappedBy = "tache")
    private Set<EmployeTache> employeTaches;

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    public Projet getProjet() { return projet; }
    public void setProjet(Projet projet) { this.projet = projet; }
    public Set<EmployeTache> getEmployeTaches() { return employeTaches; }
    public void setEmployeTaches(Set<EmployeTache> employeTaches) { this.employeTaches = employeTaches; }
}
