
import com.example.classes.Categorie;
import com.example.classes.Produit;
import com.example.service.CategorieService;
import com.example.service.ProduitService;
import com.example.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.service.*;
import java.util.Date;

public class TestApplication {
    public static void main(String[] args) {
        CategorieService catService = new CategorieService();
        ProduitService produitService = new ProduitService();
        CommandeService cmdService = new CommandeService();
        LigneCommandeService ligneService = new LigneCommandeService();

        Categorie cat = new Categorie("INF", "Informatique");
        catService.create(cat);

        Produit p1 = new Produit("ES12", 120, cat);
        Produit p2 = new Produit("ZR85", 100, cat);
        Produit p3 = new Produit("EE85", 200, cat);
        produitService.create(p1);
        produitService.create(p2);
        produitService.create(p3);

        Commande cmd = new Commande(new Date());
        cmdService.create(cmd);

        ligneService.create(new LigneCommandeProduit(p1, cmd, 7));
        ligneService.create(new LigneCommandeProduit(p2, cmd, 14));
        ligneService.create(new LigneCommandeProduit(p3, cmd, 5));

        System.out.println("\nProduits commandés dans la commande " + cmd.getId() + ":");
        produitService.findProduitsByCommande(cmd).forEach(obj ->
                System.out.println(obj[0] + "\t" + obj[1] + " DH\t" + obj[2])
        );
    }
}
