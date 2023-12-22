import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CommunauteFileManager {

    private CommunauteAgglomeration communaute;
    private static List<Pair<String, String>> routesFromFile = new ArrayList<>();

    public static List<Pair<String, String>> getRoutesFromFile() {
        return routesFromFile;
    }

    public CommunauteFileManager(CommunauteAgglomeration communaute) {
        this.communaute = communaute;
    }

    public void ouvrirFichierCommunaute(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir le fichier de communauté d'agglomération");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            chargerCommunauteDepuisFichier(file);
        }
    }

    public void chargerCommunauteDepuisFichier(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                // Pour les villes
                if (line.startsWith("ville(")) {
                    // Supposons que le nom de la ville est tout ce qui se trouve entre les
                    // parenthèses
                    String villeName = line.substring(line.indexOf('(') + 1, line.indexOf(')'));
                    communaute.ajouterVille(new Ville(villeName));
                }

                // Pour les routes
                else if (line.startsWith("route(")) {
                    // Extraire les noms des villes liées par la route (par exemple, "route(A,B).")
                    String routeContent = line.substring(line.indexOf('(') + 1, line.indexOf(')'));
                    String[] parts = routeContent.split(",");
                    if (parts.length == 2) {
                        String ville1Name = parts[0].trim();
                        String ville2Name = parts[1].trim();
                        Ville ville1 = communaute.getVille(ville1Name);
                        Ville ville2 = communaute.getVille(ville2Name);
                        if (ville1 != null && ville2 != null) {
                            communaute.ajouterRoute(ville1, ville2);
                            routesFromFile.add(new Pair<>(ville1Name, ville2Name));
                        }
                    }
                }

                // Pour les zones de recharge
                else if (line.startsWith("recharge(")) {
                    String villeName = line.substring(line.indexOf('(') + 1, line.indexOf(')'));
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
}
