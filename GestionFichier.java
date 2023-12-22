import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe gère les fichiers de la communauté d'agglomération.
 */
public class GestionFichier {

    private CommunauteAgglomeration communaute;
    private static List<Pair<String, String>> routesFromFile = new ArrayList<>();

    /**
     * Constructeur de la classe GestionFichier.
     * @param : communaute La communauté d'agglomération.
     */
    public GestionFichier(CommunauteAgglomeration communaute) {
        this.communaute = communaute;
    }

    /**
     * Retourne la liste des routes lues depuis le fichier.
     * @return : La liste des routes lues depuis le fichier.
     */
    public static List<Pair<String, String>> getRoutesFromFile() {
        return routesFromFile;
    }

    /**
     * Ouvre un fichier de communauté d'agglomération.
     * 
     * @param stage La fenêtre principale de l'application.
     */
    public File ouvrirFichierCommunaute(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir le fichier de communauté d'agglomération");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
    
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            chargerCommunauteDepuisFichier(file);
            return file;
        }
        return null;
    }
    

    /**
     * Charge la communauté d'agglomération depuis un fichier.
     * 
     * @param file Le fichier de communauté d'agglomération.
     */
    public void chargerCommunauteDepuisFichier(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String ligne;

            while ((ligne = reader.readLine()) != null) {
                // Pour les villes
                if ((ligne.startsWith("ville(") || ligne.startsWith("VILLE(")) && ligne.endsWith(").")) {
                    String nomVille = ligne.substring(6, ligne.length() - 2); // Extraire le nom entre parenthèses
                    Ville nouvelleVille = new Ville(nomVille.toUpperCase());
                    communaute.ajouterVille(nouvelleVille);
                }

                // Pour les routes
                else if (ligne.startsWith("route(") || ligne.startsWith("ROUTE(")) {
                    String[] parts = ligne.substring(6, ligne.length() - 2).split(",");
                    if (parts.length == 2) {
                        Ville ville1 = communaute.getVille(parts[0].toUpperCase());
                        Ville ville2 = communaute.getVille(parts[1].toUpperCase());
                        if (ville1 != null && ville2 != null) {
                            communaute.ajouterRoute(ville1, ville2);
                            routesFromFile.add(new Pair<>(ville1.getNom(), ville2.getNom()));
                        }
                    }
                }

                // Pour les zones de recharge
                else if (ligne.startsWith("recharge(") || ligne.startsWith("RECHARGE(")) {
                    String villeName = ligne.substring(ligne.indexOf('(') + 1, ligne.indexOf(')'));
                    Ville ville = communaute.getVille(villeName);
                    if (ville != null) {
                        ville.setZoneRecharge(true);
                    }
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        

    /**
     * Sauvegarde la communauté d'agglomération dans un fichier.
     * 
     * @param nomFichier Le nom du fichier de sauvegarde.
     */
    public void sauvegarderCommunaute(String nomFichier) {
        
        File fichier = new File(nomFichier);
    
        // Create the directory if it doesn't exist
        File repertoire = fichier.getParentFile();
        if (!repertoire.exists()) {
            repertoire.mkdirs(); // This will create the directory along with all necessary parent directories.
        }


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichier))) {
            // Écrire les informations de la communauté d'agglomération dans le fichier
            for (Ville ville : communaute.getVilles()) {
                String ligne = "ville(" + ville.getNom() + ").";
                writer.write(ligne);
                writer.newLine(); // Ajoute une nouvelle ligne après chaque enregistrement
                System.out.println("Écriture dans le fichier : " + ligne);
            }

            for (Ville ville1 : communaute.getRoutes().keySet()) {
                for (Ville ville2 : communaute.getRoutes().get(ville1)) {
                    String ligne = "route(" + ville1.getNom() + "," + ville2.getNom() + ").";
                    writer.write(ligne);
                    writer.newLine();
                    System.out.println("Écriture dans le fichier : " + ligne);
                }
            }

            for (Ville ville : communaute.getZonesRecharge()) {
                String ligne = "recharge(" + ville.getNom() + ").";
                writer.write(ligne);
                writer.newLine();
                System.out.println("Écriture dans le fichier : " + ligne);
            }

            writer.flush();

            System.out.println(" * Communauté d'agglomération sauvegardée dans " + nomFichier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
