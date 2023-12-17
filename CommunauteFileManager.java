import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.stage.FileChooser;

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
            lireFichierCommunaute(file);
        }
    }
    
    private boolean lireFichierCommunaute(File file) {
        boolean atLeastOneCityCreated = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
    
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ville(")) {
                    // Extraire le nom de la ville de la ligne (par exemple, "ville(A).")
                    String villeName = line.substring(6, 7); // Suppose que le nom de la ville est un seul caractère
                    communaute.ajouterVille(new Ville(villeName));
                    atLeastOneCityCreated = true; // Indique qu'au moins une ville a été créée
                } else if (line.startsWith("route(")) {
                    // Extraire les noms des villes liées par la route (par exemple, "route(A,B).")
                    String[] parts = line.substring(6, line.length() - 2).split(",");
                    if (parts.length == 2) {
                        routesFromFile.add(new Pair<>(parts[0], parts[1]));
                    }
                } else if (line.startsWith("recharge(")) {
                    // Extraire le nom de la ville avec une zone de recharge (par exemple, "recharge(B).")
                    String villeName = line.substring(9, 10); // Suppose que le nom de la ville est un seul caractère
                    Ville ville = communaute.getVille(villeName);
                    if (ville != null) {
                        ville.setZoneRecharge(true);
                    }
                }
            }
    
            reader.close();
            // Mettre à jour l'interface utilisateur ou effectuer d'autres actions nécessaires ici
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs de lecture du fichier ici
        }
    
        return atLeastOneCityCreated;
    }

    
    
    
    

}
