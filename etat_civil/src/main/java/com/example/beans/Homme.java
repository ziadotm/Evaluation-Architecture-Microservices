package com.example.beans;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "homme")
public class Homme extends Personne {

    @OneToMany(mappedBy = "homme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mariage> mariages;

    public List<Mariage> getMariages() { return mariages; }
    public void setMariages(List<Mariage> mariages) { this.mariages = mariages; }
}

