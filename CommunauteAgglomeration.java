
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

// Classe représentant une communauté d'agglomération
public class CommunauteAgglomeration {

  // Nombre de villes dans la communauté d'agglomération
  private int nombreVilles;
  // Map associant à chaque ville une liste de villes adjacentes
  private Map<Ville, List<Ville>> mapVilles;

  // Scanner pour lire les entrées de l'utilisateur dans les méthodes demandant
  // des saisies à l'utilisateur
  private Scanner scanner;

  // Constructeur de la classe
  public CommunauteAgglomeration(int nombreVilles) {
    this.nombreVilles = nombreVilles;
    this.mapVilles = new HashMap<Ville, List<Ville>>();
    this.scanner = new Scanner(System.in);
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

  // Méthode pour supprimer une zone de recharge d'une ville
  public void supprimerZoneRecharge(Ville ville) {
    if (ville.aZoneRecharge()) {
      // Vérifier si des villes voisines ont encore une zone de recharge
      List<Ville> villesVoisines = getVillesAdjacentes(ville);

      boolean voisinsAvecZone = false;

      for (Ville voisine : villesVoisines) {
        if (voisine.aZoneRecharge()) {
          voisinsAvecZone = true;
          break;
        }
      }

      if (voisinsAvecZone) {
        ville.setZoneRecharge(false);
        System.out.println("La zone de recharge a été supprimée de la ville " + ville.getNom());
      } else {
        System.out.println(
            "Vous ne pouvez pas supprimer une zone de recharge si les villes voisines n'ont pas de zone ou n'ont accès à aucun zone dans les villes voisines.");
      }
    } else {
      System.out.println("Vous ne pouvez supprimer une zone de recharge qui n'existe pas");
    }
  }


  // Méthode pour afficher un menu permettant d'ajouter ou de supprimer une route
  public void menuAjouterSupprimerRoute() {
    int choix;
    do {
      System.out.println();
      System.out.println("1. Ajouter une route");
      System.out.println("2. Supprimer une route");
      System.out.println("3. Quitter");
      System.out.print("Votre choix : ");
      choix = scanner.nextInt();
      scanner.nextLine();

      if (choix == 1 || choix == 2) {
        afficherVilles();
        System.out.println("Vous allez rajouter ou supprimer une route entre deux villes. ");

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
        // sc.close();
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
      switch (choix) {
        case 1:
          System.out.println("Entrez le nom de la ville : ");
          String nomVille = scanner.nextLine();
          nomVille = nomVille.toUpperCase();

          Ville villeAj = null;

          for (Ville v : getVilles()) {
            if (v.getNom().equals(nomVille)) {
              System.out.println("Ville trouvée");
              villeAj = v;
            }

          } // une exception a gere si les villes ne sont pas trouvees
          ajouterZoneRecharge(villeAj);
          break;
        case 2:
          System.out.println("Entrez le nom de la ville : ");
          nomVille = scanner.nextLine();
          nomVille = nomVille.toUpperCase();

          Ville villeSupp = null;

          for (Ville v : getVilles()) {
            if (v.getNom().equals(nomVille)) {
              System.out.println("Ville trouvée");
              villeSupp = v;
              break; // Sortir de la boucle une fois que la ville est trouvée
            }

          } // une exception a gérer si les villes ne sont pas trouvees
          supprimerZoneRecharge(villeSupp);
          break;
        case 3:
          System.out.println("Le menu a été quitté avec succès ");
          break;
        default:
          System.out.println("Choix invalide");
          break;
      }
    } while (choix != 3);

  }

  public void closeScanner() {
    if (scanner != null) {
      scanner.close();
    }
  }

}
