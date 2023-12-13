import java.util.Scanner;

// Classe pour tester la communauté d'agglomération contenant la méthode main()
public class Main {

  private static final String MESSAGE_ENTRER_NOMBRE_VILLES = "Entrez le nombre de villes : ";
  private static final String MESSAGE_SAISIE_INCORRECTE = "Veuillez entrer un nombre positif ou égal à 1 et inférieur ou égal à 26.";

  private static Scanner sc = new Scanner(System.in);

  /*
   * Méthode main() pour tester la communauté d'agglomération
   */
  public static void main(String[] args) {
    int nombreVilles = saisirNombreVilles();
    CommunauteAgglomeration communauteA = initialiserCommunaute(nombreVilles);
    ajouterVilles(communauteA, nombreVilles);
    afficherInformations(communauteA);
    fermerScanner();
    System.out.println("Au revoir !!!");
  }








  // -------- Méthodes secondaires utilisées dans main() --------

  /*
   * Méthode pour saisir le nombre de villes de la communauté d'agglomération
   */
  private static int saisirNombreVilles() {
    int nombreVilles;

    do {
        System.out.print(MESSAGE_ENTRER_NOMBRE_VILLES);

        // Vérifier si l'entrée suivante est un entier
        while (!sc.hasNextInt()) {
            System.out.println("Veuillez entrer un nombre entier.");
            System.out.print(MESSAGE_ENTRER_NOMBRE_VILLES);
            sc.next(); // Consommer l'entrée invalide
        }

        // Lire l'entier
        nombreVilles = sc.nextInt();

        if (nombreVilles < 1 || nombreVilles > 26) {
            System.out.println(MESSAGE_SAISIE_INCORRECTE);
        }

    } while (nombreVilles < 1 || nombreVilles > 26);

    return nombreVilles;
}


  private static CommunauteAgglomeration initialiserCommunaute(int nombreVilles) {
    return new CommunauteAgglomeration(nombreVilles);
  }

  /*
   * Méthode pour ajouter les villes à la communauté d'agglomération
   * le nommage des villes se fait par ordre alphabétique
   * en commençant par la lettre A1 à Z1 puis A2 à Z2 et ainsi de suite
   */
  private static void ajouterVilles(CommunauteAgglomeration communauteA, int nombreVilles) {
    int nombreLettres = 26;

    for (int i = 0; i < nombreVilles; i++) {
      char lettre = (char) ('A' + (i % nombreLettres));
      int indice = (i / nombreLettres) + 1; // On ajoute 1 car les indices commencent généralement à 1
      String nomVille = lettre + String.valueOf(indice);
      Ville ville = new Ville(nomVille);
      communauteA.ajouterVille(ville);
    }
  }

  /*
   * Affichage des informations en rapport avec la communauté d'agglomération
   * Méthode pour l'affichage des villes de la communauté d'agglomération
   * affichage des menus pour ajouter ou supprimer une route ou une zone de
   * recharge
   * etc.
   */
  private static void afficherInformations(CommunauteAgglomeration communauteA) { // à modifier en fonction des besoins
                                                                                  // d'affichage
    communauteA.afficherVilles();
    System.out.println();
    communauteA.menuAjouterSupprimerRoute();
    System.out.println();
    communauteA.menuAjouterSupprimerZoneRecharge();
    communauteA.afficherRoutes();
    communauteA.afficherVillesAvecZoneRecharge();

    System.out.println();
    System.out.println("fin du programme.");
  }

  /*
   * Méthode pour fermer le scanner
   */
  private static void fermerScanner() {
    if (sc != null) {
      sc.close();
    }
  }

}
