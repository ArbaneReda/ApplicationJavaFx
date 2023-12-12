import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.List;
import javafx.stage.Stage;
import javafx.stage.FileChooser;


public class CommunauteFileManager {
    
    private CommunauteAgglomeration communaute;
    
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
    

    private void lireFichierCommunaute(File file) {
        // Lire le fichier ligne par ligne
    }
    
}
