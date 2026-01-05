package com.example.classes;

import com.example.classes.EmployeTache;
import com.example.classes.Projet;
import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private String prenom;
    private String telephone;

    @OneToMany(mappedBy = "employe")
    private Set<EmployeTache> employeTaches;

    @OneToMany(mappedBy = "chefProjet")
    private Set<Projet> projets;

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public Set<EmployeTache> getEmployeTaches() { return employeTaches; }
    public void setEmployeTaches(Set<EmployeTache> employeTaches) { this.employeTaches = employeTaches; }
    public Set<Projet> getProjets() { return projets; }
    public void setProjets(Set<Projet> projets) { this.projets = projets; }
}
