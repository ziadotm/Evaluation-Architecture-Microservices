package com.example.beans;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "femme")
@NamedQueries({
        @NamedQuery(
                name = "Femme.findMarriedAtLeastTwice",
                query = "SELECT f FROM Femme f WHERE (SELECT COUNT(m) FROM Mariage m WHERE m.femme = f) >= 2"
        )
})
public class Femme extends Personne {

    @OneToMany(mappedBy = "femme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mariage> mariages;

    public List<Mariage> getMariages() { return mariages; }
    public void setMariages(List<Mariage> mariages) { this.mariages = mariages; }
}

