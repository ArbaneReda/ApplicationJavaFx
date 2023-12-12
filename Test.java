import java.util.Scanner;

// Classe pour tester la communauté d'agglomération contenant la méthode main()
public class Test {
  public static void main(String[] args) {
    int nombreVilles;
    Scanner sc = new Scanner(System.in);

    // Demande à l'utilisateur de saisir le nombre de villes
    do {
      System.out.print("Entrez le nombre de villes : ");
      nombreVilles = sc.nextInt();
    } while (nombreVilles < 1 || nombreVilles > 26);

    // Crée une nouvelle communauté d'agglomération avec le nombre de villes
    // spécifié
    CommunauteAgglomeration communauteA = new CommunauteAgglomeration(nombreVilles);

    // Ajoute les villes à la communauté d'agglomération
    for (int i = 0; i < nombreVilles; i++) {
      Ville ville = new Ville("" + (char) ('A' + i));
      communauteA.ajouterVille(ville);
    }

    // Affiche les villes de la communauté d'agglomération
    communauteA.afficherVilles();
    System.out.println();
    // Affiche le menu pour ajouter ou supprimer une route
    //communauteA.menuAjouterSupprimerRoute();
    System.out.println();
    // Affiche le menu pour ajouter ou supprimer une zone de recharge
    //communauteA.menuAjouterSupprimerZoneRecharge();
    // Affiche les routes de la communauté d'agglomération
    communauteA.afficherRoutes();
    // Affiche les villes avec une zone de recharge
    communauteA.afficherVillesAvecZoneRecharge();
    // Ferme le scanner correspondant à la communauté d'agglomération
   // communauteA.closeScanner();
    // Ferme le scanner
    sc.close();

    System.out.println();
    System.out.println();
    System.out.println("fin du programme. Au revoir !!!!");

  }

}
