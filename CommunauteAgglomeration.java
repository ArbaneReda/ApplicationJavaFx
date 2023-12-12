
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
//import java.util.Scanner;

// Classe représentant une communauté d'agglomération
public class CommunauteAgglomeration {

  // Nombre de villes dans la communauté d'agglomération
  private int nombreVilles;
  // Map associant à chaque ville une liste de villes adjacentes
  private Map<Ville, List<Ville>> mapVilles;

  // Scanner pour lire les entrées de l'utilisateur dans les méthodes demandant
  // des saisies à l'utilisateur
  //private Scanner scanner;

  // Constructeur de la classe
  public CommunauteAgglomeration(int nombreVilles) {
    this.nombreVilles = nombreVilles;
    this.mapVilles = new HashMap<Ville, List<Ville>>();
    //this.scanner = new Scanner(System.in);
  }

  // Méthode pour ajouter une ville à la communauté d'agglomération
  public void ajouterVille(Ville ville) {
    this.mapVilles.put(ville, new ArrayList<Ville>());
  }

  // Méthode pour ajouter une route entre deux villes
  public boolean ajouterRoute(Ville ville1, Ville ville2) {
    if (!this.mapVilles.get(ville1).contains(ville2) && !this.mapVilles.get(ville2).contains(ville1)) {
      this.mapVilles.get(ville1).add(ville2);
      this.mapVilles.get(ville2).add(ville1);
      System.out.println("Route ajoutée entre " + ville1.getNom() + " et " + ville2.getNom() + ".");
      return true;
    } else {
      System.out.println("Une route existe déjà entre ces deux villes.");
      return false;
    }
  }

  // Méthode pour supprimer une route entre deux villes
  public boolean supprimerRoute(Ville ville1, Ville ville2) {
    if (this.mapVilles.get(ville1).contains(ville2) && this.mapVilles.get(ville2).contains(ville1)) {
      this.mapVilles.get(ville1).remove(ville2);
      this.mapVilles.get(ville2).remove(ville1);
      System.out.println("Route supprimée entre " + ville1.getNom() + " et " + ville2.getNom() + ".");
      return true;
    } else {
      System.out.println("Impossible de supprimer une route inexistante entre ces deux villes.");
      return false;
    }
  }

  // Méthode pour obtenir la liste des villes adjacentes à une ville donnée
  public List<Ville> getVillesAdjacentes(Ville ville) {
    return this.mapVilles.get(ville);
  }

  // Méthode pour obtenir la liste des villes de la communauté d'agglomération
  public List<Ville> getVilles() {
    return new ArrayList<Ville>(this.mapVilles.keySet());
  }

  // Méthode pour afficher la liste des villes
  public void afficherVilles() {
    System.out.println("Liste des villes : ");
    for (Ville ville : this.mapVilles.keySet()) {
      System.out.print(ville.getNom() + " ");
    }
  }

  // Méthode pour afficher la liste des routes
  public void afficherRoutes() {
    System.out.println("Liste des routes : ");
    for (Ville ville : this.mapVilles.keySet()) {
      System.out.print(ville.getNom() + " : ");
      for (Ville villeAdjacente : this.mapVilles.get(ville)) {
        System.out.print(villeAdjacente.getNom() + " ");
      }
      System.out.println();
    }
  }

  // Méthode pour afficher la liste des villes avec une zone de recharge
  public void afficherVillesAvecZoneRecharge() {
    System.out.println("Liste des villes avec zone de recharge : ");
    for (Ville ville : this.mapVilles.keySet()) {
      if (ville.aZoneRecharge()) {
        System.out.print(ville.getNom() + " ");
      }
    }
    System.out.println();
  }

  // Méthode pour obtenir le nombre de villes
  public int getNombreVilles() {
    return this.nombreVilles;
  }


  // Méthode pour ajouter une zone de recharge à une ville
  public boolean ajouterZoneRecharge(Ville ville) {
    if (!ville.aZoneRecharge()) {
      ville.setZoneRecharge(true);
      System.out.println("Zone de recharge ajoutée à la ville " + ville.getNom());
      return true;
    } else {
      System.out.println("La ville " + ville.getNom() + " possède déjà une zone de recharge.");
      return false;
    }
  }


    // Méthode qui retourne le nombre de zones de recharge adjacentes à une ville
  // donnée
  // utilisée dans supprimerZoneRecharge()
  /*public int nombreZonesRechargeAdjacents(Ville ville) {
    int nombreZonesRechargeAdjacents = 0;
    for (Ville villeAdjacente : getVillesAdjacentes(ville)) {
      if (villeAdjacente.aZoneRecharge()) {
        nombreZonesRechargeAdjacents++;
      }
    }
    return nombreZonesRechargeAdjacents;
  }

  // Méthode pour supprimer une zone de recharge d'une ville
  public void supprimerZoneRecharge(Ville ville) {
    if (!ville.aZoneRecharge()) {
      System.out.println("Vous ne pouvez pas supprimer une zone de recharge qui n'existe pas!!!");
      return;
    }
    // le cas où la ville a une zone de recharge

    boolean condition = false;

    if (nombreZonesRechargeAdjacents(ville) == 1) {
      for (Ville v : getVillesAdjacentes(ville)) {
        if ((nombreZonesRechargeAdjacents(v)) == 1) {// il y a un voisin de voisin qui a aucune zone de recharge (car on
                                                     // ne compte pas la ville en question)
          condition = true;//donc ce n'est pas possible
          break;
        }
      }
    }

    if (condition) {
      System.out.println(
          "Vous ne pouvez pas supprimer cette zone de recharge car sinon un de ses voisins sera déconnécté d'une zone de recharge!!!");
      return;
    }

    ville.setZoneRecharge(false);// tout est vérifié et donc il est possible de supprimer la zone de recharge
    System.out.println("La zone de recharge a été supprimée de la ville " + ville.getNom());
  }


  // Méthode pour afficher un menu permettant d'ajouter ou de supprimer une route
  public void menuAjouterSupprimerRoute() {
    int choix;
    do {
      System.out.println();
      System.out.println("1. Ajouter une route");
      System.out.println("2. Supprimer une route");
      System.out.println("3. Menu suivant");
      System.out.print("Votre choix : ");
      choix = scanner.nextInt();
      scanner.nextLine();

      if (choix == 1 || choix == 2) {
        afficherVilles();
        System.out.println("Vous allez ajouter ou supprimer une route entre deux villes. ");

        System.out.println("Entrez le nom de la première ville : ");
        String nomVille1 = scanner.nextLine();
        nomVille1 = nomVille1.toUpperCase();
        System.out.println("Entrez le nom de la deuxième ville : ");
        String nomVille2 = scanner.nextLine();
        nomVille2 = nomVille2.toUpperCase();

        Ville v1 = null;
        Ville v2 = null;

        for (Ville ville : getVilles()) {
          if (ville.getNom().equals(nomVille1)) {
            System.out.println("Ville " + ville.getNom() + " trouvée");
            v1 = ville;
          }
          if (ville.getNom().equals(nomVille2)) {
            System.out.println("Ville " + ville.getNom() + " trouvée");
            v2 = ville;
          }
        } // une exception a gere si les villes ne sont pas trouvees

        if (choix == 1) {
          ajouterRoute(v1, v2);
        } else if (choix == 2) {
          supprimerRoute(v1, v2);
        }
      } else if (choix == 3) {
        System.out.println();
        System.out.println("Passage au menu suivant: ");
        return;
      } else {
        System.out.println("Choix non-valable");
      }

    } while (true);

  }

  // Méthode pour afficher un menu permettant d'ajouter ou de supprimer une zone
  // de recharge
  public void menuAjouterSupprimerZoneRecharge() {
    int choix;
    do {
      System.out.println("1. Ajouter une zone de recharge");
      System.out.println("2. Supprimer une zone de recharge");
      System.out.println("3. Quitter");
      System.out.print("Entrez votre choix : ");
      choix = scanner.nextInt();
      scanner.nextLine();


       if (choix == 1 || choix == 2) {
        afficherVilles();
        System.out.println("Vous allez ajouter ou supprimer une zone de recharge entre deux villes. ");

        System.out.println("Entrez le nom de la ville : ");
        String nomVille = scanner.nextLine();
        nomVille = nomVille.toUpperCase();
        

        Ville v = null;
        

        for (Ville ville : getVilles()) {
          if (ville.getNom().equals(nomVille)) {
            System.out.println("Ville " + ville.getNom() + " trouvée");
            v = ville;
          }
        } // une exception a gere si les villes ne sont pas trouvees

        if (choix == 1) {
          ajouterZoneRecharge(v);
        } else if (choix == 2) {
          supprimerZoneRecharge(v);
        }
      } else if (choix == 3) {
        System.out.println();
        System.out.println("Quitter ");
        return;
      } else {
        System.out.println("Choix non-valable");
      }

    } while (true);

  }

  public void closeScanner() {
    if (scanner != null) {
      scanner.close();
    }
  }

}*/
}