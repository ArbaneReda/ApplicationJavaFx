import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    public void start(Stage primaryStage) {
        // Chargez l'image de fond depuis le fichier
        Image backgroundImage = new Image("file:images/photovilleblur.jpg");
        ImageView backgroundView = new ImageView(backgroundImage);

        // Obtenir les dimensions de l'image de fond pour définir la taille de la scène
        double imageWidth = backgroundImage.getWidth();
        double imageHeight = backgroundImage.getHeight();

        // Créez un StackPane comme racine
        StackPane root = new StackPane();

        // Créez l'ImageView de l'image de fond et ajoutez-la à la StackPane
        root.getChildren().add(backgroundView);

        // Créez une VBox pour les boutons sur la première page
        VBox buttonBox = new VBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        // Ajoutez le bouton pour passer à la deuxième page et la logique d'événement
        Button startButton = new Button("Entrer");
        Button exitButton = new Button("Quitter");

        startButton.setStyle("-fx-font-size: 18px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        exitButton.setStyle("-fx-font-size: 18px; -fx-background-color: #f44336; -fx-text-fill: white;");

        startButton.setOnAction(e -> showSecondPage(primaryStage));
        exitButton.setOnAction(e -> primaryStage.close());

        // Définissez une taille fixe pour les boutons
        startButton.setMinSize(150, 40);
        exitButton.setMinSize(150, 40);

        buttonBox.getChildren().addAll(startButton, exitButton);

        // Calculez les marges pour centrer la VBox et décaler légèrement vers la gauche
        double xOffset = (imageWidth - buttonBox.getBoundsInParent().getWidth()) / 2.5;
        double yOffset = (imageHeight - buttonBox.getBoundsInParent().getHeight()) / 2;

        // Appliquez les marges à la VBox
        buttonBox.setLayoutX(xOffset);
        buttonBox.setLayoutY(yOffset);

        // Créez l'ImageView de l'image PNG (texte)
        Image overlayImage = new Image("file:images/texte.png");
        ImageView overlayImageView = new ImageView(overlayImage);

        // Créez un Pane pour contenir la VBox des boutons et l'image PNG
        Pane pane = new Pane();
        pane.getChildren().addAll(backgroundView, overlayImageView, buttonBox);

        // Créez un StackPane pour superposer le Pane
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(pane);

        // Ajustez la taille de l'image PNG proportionnellement
        double scaleFactor = Math.min(imageWidth / overlayImage.getWidth(), imageHeight / overlayImage.getHeight());
        overlayImageView.setFitWidth(overlayImage.getWidth() * scaleFactor);
        overlayImageView.setFitHeight(overlayImage.getHeight() * scaleFactor);

        Scene scene = new Scene(stackPane, imageWidth, imageHeight);

        primaryStage.setTitle("Trouvez le nombre optimal de zones de recharge en fonction de villes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showSecondPage(Stage primaryStage) {
        // Créez une instance de CommunauteAgglomeration
        CommunauteAgglomeration communaute = new CommunauteAgglomeration(0); // ici, nous initialisons avec 0 villes

        // Créez une instance de la classe SecondPage en passant la scène principale et
        // l'instance de CommunauteAgglomeration
        SecondPage secondPage = new SecondPage(primaryStage, primaryStage.getScene(), communaute);

        // Affichez la deuxième page
        secondPage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
