import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.scene.Node;

public class SecondPage {

    private final Stage primaryStage;
    private final Scene mainScene;
    private final CommunauteAgglomeration communaute;
    private Map<String, Circle> villeCircles;
    private CommunauteFileManager fileManager;

    // Constructeur de la classe
    public SecondPage(Stage primaryStage, Scene mainScene, CommunauteAgglomeration communaute) {
        this.primaryStage = primaryStage;
        this.mainScene = mainScene;
        this.communaute = communaute;
        this.villeCircles = new HashMap<>();
        this.fileManager = new CommunauteFileManager(communaute);
    }

    // Méthode pour afficher la deuxième page
    public void show() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(15));
        borderPane.setStyle("-fx-background-color: #FAFAFA;");

        Pane graphPane = createStyledPane(600, 400);
        TextArea terminalOutputPane = createTextArea();

        borderPane.setCenter(graphPane);
        borderPane.setBottom(terminalOutputPane);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15, 0, 15, 0));

        Button startButton = createButton("Commencer", "#4CAF50");
        Button backButton = createButton("Retourner", "#6c7ae0");
        
        startButton.setOnAction(e -> handleStart(graphPane, terminalOutputPane, buttonBox));
        backButton.setOnAction(e -> primaryStage.setScene(mainScene));
        

        buttonBox.getChildren().addAll(startButton, backButton);
        borderPane.setBottom(buttonBox);

        Scene secondPageScene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(secondPageScene);
        primaryStage.show();
    }

    // Méthode pour créer un Pane avec un style CSS personnalisé
    private Pane createStyledPane(double width, double height) {
        Pane pane = new Pane();
        pane.setMinSize(width, height);
        pane.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1;");
        pane.setEffect(new DropShadow(5, Color.GREY));
        return pane;
    }

    // Méthode pour créer une zone de texte avec un style CSS personnalisé
    private TextArea createTextArea() {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle(
                "-fx-font-size: 14px; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #FAFAFA;");
        textArea.setEffect(new DropShadow(5, Color.GREY));
        return textArea;
    }

    // Méthode pour créer un bouton avec un style CSS personnalisé
    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(new Font("Arial", 16));
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
        button.setPadding(new Insets(10, 20, 10, 20));
        button.setEffect(new DropShadow(3, Color.rgb(50, 50, 50, 0.5)));
        button.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, new CornerRadii(5),
                BorderWidths.DEFAULT)));
        return button;
    }

    // Méthode pour gérer le début du programme
    private void handleStart(Pane graphPane, TextArea terminalOutputPane, HBox buttonBox) {
        buttonBox.getChildren().clear(); // Nettoyer les boutons existants
    
        Button manuelButton = createButton("Manuellement", "#4CAF50");
        Button fichierButton = createButton("Fichier", "#2196F3");
    
        manuelButton.setOnAction(e -> saisieManuelle(graphPane, terminalOutputPane, buttonBox));
        fichierButton.setOnAction(e -> {
            fileManager.ouvrirFichierCommunaute(primaryStage);
            // Après avoir lu les données du fichier, appelez displayGraph pour afficher les villes
            showRoutesMenu(buttonBox, graphPane, terminalOutputPane,true);
            displayGraph(graphPane, terminalOutputPane);
        });
    
        buttonBox.getChildren().addAll(manuelButton, fichierButton);
    }

    private void saisieManuelle(Pane graphPane, TextArea terminalOutputPane, HBox buttonBox) {
        TextInputDialog numberDialog = new TextInputDialog();
        numberDialog.setTitle("Nombre de villes");
        numberDialog.setHeaderText("Création des villes");
        numberDialog.setContentText("Entrez le nombre de villes :");
    
        Optional<String> numberResult = numberDialog.showAndWait();
        numberResult.ifPresent(number -> {
            try {
                int nbVilles = Integer.parseInt(number);
                char villeName = 'A';
                for (int i = 0; i < nbVilles; i++) {
                    String nomVille = String.valueOf((char) (villeName + i));
                    communaute.ajouterVille(new Ville(nomVille));
                }
                terminalOutputPane.appendText("Villes créées : " + nbVilles + "\n");
    
                if (!communaute.getVilles().isEmpty()) {
                    showRoutesMenu(buttonBox, graphPane, terminalOutputPane, true);
                } else {
                    showAlert("Aucune ville", "Aucune ville n'a été ajoutée à la communauté.");
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez entrer un nombre entier valide.");
                alert.showAndWait();
            }
        });
    }
    

    // Méthode pour mettre à jour l'interface utilisateur pour les routes
    private void showRoutesMenu(HBox buttonBox, Pane graphPane, TextArea terminalOutputPane, boolean clearGraph) {
        buttonBox.getChildren().clear();
    
        Button addRouteButton = createButton("Ajouter une route", "#2196F3");
        Button removeRouteButton = createButton("Supprimer une route", "#F44336");
        Button nextMenuButton = createButton("Menu Suivant", "#FFA500");
        Button quitButton = createButton("Quitter", "#9E9E9E");
    
        addRouteButton.setOnAction(e -> addRoute(graphPane));
        removeRouteButton.setOnAction(e -> removeRoute(graphPane));
        nextMenuButton.setOnAction(e -> showRechargeZoneMenu(graphPane, terminalOutputPane, buttonBox));
        quitButton.setOnAction(e -> primaryStage.setScene(mainScene));
    
        buttonBox.getChildren().addAll(addRouteButton, removeRouteButton, nextMenuButton, quitButton);
    
        // Afficher le graphe seulement si nécessaire
        if (clearGraph) {
            displayGraph(graphPane, terminalOutputPane);
        }
    }

    // Méthode pour afficher le menu de la zone de recharge
    private void showRechargeZoneMenu(Pane graphPane, TextArea terminalOutputPane, HBox buttonBox) {
        // Nettoyer l'interface utilisateur actuelle
        buttonBox.getChildren().clear();
        terminalOutputPane.clear();

        // boutons pour les actions de la zone de recharge
        Button addRechargeZoneButton = createButton("Ajouter une zone de recharge", "#4CAF50");
        Button removeRechargeZoneButton = createButton("Supprimer une zone de recharge", "#F44336");
        Button backButton = createButton("Retour", "#6c7ae0");

        addRechargeZoneButton.setOnAction(e -> addRechargeZone(graphPane));

        removeRechargeZoneButton.setOnAction(e -> removeRechargeZone(graphPane));

        backButton.setOnAction(e -> showRoutesMenu(buttonBox, graphPane, terminalOutputPane, false));

        // Ajouter les nouveaux boutons à la HBox
        buttonBox.getChildren().addAll(addRechargeZoneButton, removeRechargeZoneButton, backButton);
    }

    private void addRoute(Pane graphPane) {
        List<Ville> villes = communaute.getVilles();
        ChoiceDialog<Ville> dialog = new ChoiceDialog<>(villes.get(0), villes);
        dialog.setTitle("Ajouter une route");
        dialog.setHeaderText("Sélectionnez la première ville:");
        Optional<Ville> result = dialog.showAndWait();
    
        result.ifPresent(ville1 -> {
            ChoiceDialog<Ville> dialog2 = new ChoiceDialog<>(villes.get(1), villes);
            dialog2.setTitle("Ajouter une route");
            dialog2.setHeaderText("Sélectionnez la deuxième ville:");
            Optional<Ville> result2 = dialog2.showAndWait();
    
            result2.ifPresent(ville2 -> {
                if (ville1.equals(ville2)) {
                    showAlert("Erreur d'ajout de route",
                            "Impossible d'ajouter une route à la même ville (" + ville1.getNom() + ").");
                } else {
                    boolean wasAdded = communaute.ajouterRoute(ville1, ville2);
                    if (wasAdded) {
                        drawRoute(graphPane, ville1, ville2); // Appel de drawRoute ici
                    } else {
                        showAlert("Ajout de route",
                                "Une route existe déjà entre " + ville1.getNom() + " et " + ville2.getNom() + ".");
                    }
                }
            });
        });
    }
    
    

    // Méthode pour supprimer une route
    private void removeRoute(Pane graphPane) {
        List<Ville> villes = communaute.getVilles();
        if (villes.size() < 2) {
            showAlert("Suppression de route", "Il doit y avoir au moins deux villes pour supprimer une route.");
            return;
        }

        ChoiceDialog<Ville> dialog = new ChoiceDialog<>(villes.get(0), villes);
        dialog.setTitle("Supprimer une route");
        dialog.setHeaderText("Sélectionnez la première ville:");
        Optional<Ville> result = dialog.showAndWait();

        result.ifPresent(ville1 -> {
            ChoiceDialog<Ville> dialog2 = new ChoiceDialog<>(villes.get(1), villes);
            dialog2.setTitle("Supprimer une route");
            dialog2.setHeaderText("Sélectionnez la deuxième ville:");
            Optional<Ville> result2 = dialog2.showAndWait();

            result2.ifPresent(ville2 -> {
                boolean wasRemoved = communaute.supprimerRoute(ville1, ville2);
                if (wasRemoved) {
                    updateGraph(graphPane, ville1, ville2);
                } else {
                    showAlert("Suppression de route",
                            "Il n'existe pas de route entre " + ville1.getNom() + " et " + ville2.getNom() + ".");
                }
            });
        });
    }

    // pour mettre à jour le graphique après la suppression d'une route
    private void updateGraph(Pane graphPane, Ville ville1, Ville ville2) {
        // On uniquement la ligne correspondant à la route supprimée
        Line toRemove = null;
        for (Node node : graphPane.getChildren()) {
            if (node instanceof Line) {
                Line line = (Line) node;
                if ((line.getStartX() == villeCircles.get(ville1.getNom()).getCenterX() &&
                        line.getStartY() == villeCircles.get(ville1.getNom()).getCenterY() &&
                        line.getEndX() == villeCircles.get(ville2.getNom()).getCenterX() &&
                        line.getEndY() == villeCircles.get(ville2.getNom()).getCenterY()) ||
                        (line.getEndX() == villeCircles.get(ville1.getNom()).getCenterX() &&
                                line.getEndY() == villeCircles.get(ville1.getNom()).getCenterY() &&
                                line.getStartX() == villeCircles.get(ville2.getNom()).getCenterX() &&
                                line.getStartY() == villeCircles.get(ville2.getNom()).getCenterY())) {
                    toRemove = line;
                    break;
                }
            }
        }

        if (toRemove != null) {
            graphPane.getChildren().remove(toRemove);
        }
    }

    // méthode pour ajouter une zone de recharge
    private void addRechargeZone(Pane graphPane) {
        List<Ville> villes = communaute.getVilles();
        ChoiceDialog<Ville> dialog = new ChoiceDialog<>(villes.get(0), villes);
        dialog.setTitle("Ajouter une zone de recharge");
        dialog.setHeaderText("Sélectionnez une ville:");
        Optional<Ville> result = dialog.showAndWait();

        result.ifPresent(ville -> {
            boolean wasZoneAdded = communaute.ajouterZoneRecharge(ville);
            if (wasZoneAdded) {
                updateCircleColor(ville, Color.GREEN); // Changer la couleur en vert si la zone de recharge a été
                                                       // ajoutée
            } else {
                showAlert("Zone de recharge", "La ville " + ville.getNom() + " a déjà une zone de recharge.");
            }
        });
    }

    // méthode pour supprimer une zone de recharge
    private void removeRechargeZone(Pane graphPane) {
        List<Ville> villes = communaute.getVilles();
        ChoiceDialog<Ville> dialog = new ChoiceDialog<>(villes.get(0), villes);
        dialog.setTitle("Supprimer une zone de recharge");
        dialog.setHeaderText("Sélectionnez une ville:");
        Optional<Ville> result = dialog.showAndWait();

        // si une ville n'a pas de voisin, c'est à dire qu'elle est isolée, on ne peut pas supprimer sa zone de recharge
        
    
        result.ifPresent(ville -> { // ifPresent() est appelé si l'utilisateur a sélectionné une ville
            if (!ville.aZoneRecharge()) {
                showAlert("Suppression de zone de recharge",
                        "La ville " + ville.getNom() + " n'a pas de zone de recharge à supprimer.");
                return;
            }
             // si une ville n'a pas de voisin, c'est à dire qu'elle est isolée, on ne peut pas supprimer sa zone de recharge
            if (communaute.getVillesAdjacentes(ville).isEmpty()) {
                showAlert("Suppression de zone de recharge",
                        "Vous ne pouvez pas supprimer la zone de recharge de " + ville.getNom() + 
                        " car la ville est isolée.");
                return;
            }

            // On vérifie si la suppression de la zone de recharge de 'ville' isole une ville voisine
            for (Ville voisine : communaute.getVillesAdjacentes(ville)) {
                // Si 'voisine' n'a pas de zone de recharge et que toutes les villes adjacentes à 'voisine', sauf 'ville',
                // n'ont pas de zone de recharge, alors on ne peut pas supprimer la zone de recharge de 'ville'
                if (!voisine.aZoneRecharge() && communaute.getVillesAdjacentes(voisine).stream() // .stream() est utilisé pour convertir la liste en flux
                        .filter(v -> !v.equals(ville)) // On exclut 'ville' de la vérification
                        .noneMatch(Ville::aZoneRecharge)) { // renvoie true si aucune ville n'a de zone de recharge
                    showAlert("Suppression de zone de recharge",
                            "Vous ne pouvez pas supprimer la zone de recharge de " + ville.getNom() + 
                            " car la ville " + voisine.getNom() + " serait isolée.");
                    return;
                }
            }
    
            // Si la vérification est passée, on peut supprimer la zone de recharge
            ville.setZoneRecharge(false);
            updateCircleColor(ville, Color.BLUE);
            showAlert("Zone de recharge", "Zone de recharge supprimée pour la ville " + ville.getNom() + ".");
        });
    }


    // Cette méthode met à jour la couleur du cercle représentant la ville
    private void updateCircleColor(Ville ville, Color color) {
        Circle circle = villeCircles.get(ville.getNom());
        if (circle != null) {
            circle.setFill(color);
        }
    }

    // Cette méthode affiche une boîte de dialogue d'alerte
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Cette méthode dessine une route entre deux villes
    private void drawRoute(Pane graphPane, Ville ville1, Ville ville2) {
        Circle circle1 = villeCircles.get(ville1.getNom());
        Circle circle2 = villeCircles.get(ville2.getNom());

        if (circle1 != null && circle2 != null) {
            Line line = new Line(circle1.getCenterX(), circle1.getCenterY(),
                    circle2.getCenterX(), circle2.getCenterY());
            line.setStroke(Color.BLACK);
            graphPane.getChildren().add(line);
        }
    }

    // Cette méthode affiche le graphique
    // Cette méthode affiche le graphique
    private void displayGraph(Pane graphPane, TextArea terminalOutputPane) {
        terminalOutputPane.appendText("Affichage du graphe...\n");

        graphPane.getChildren().clear();

        List<Ville> villes = communaute.getVilles();

        // Centre du cercle
        double centerX = graphPane.getWidth() / 2;
        double centerY = graphPane.getHeight() / 2;
        double radius = Math.min(centerX, centerY) - 50; // Rayon du cercle sur lequel les villes seront placées
        double angleStep = 360.0 / villes.size();

        for (int i = 0; i < villes.size(); i++) {
            Ville ville = villes.get(i);

            // Calcul des coordonnées x et y pour positionner la ville
            double angle = angleStep * i;
            double x = centerX + radius * Math.cos(Math.toRadians(angle));
            double y = centerY + radius * Math.sin(Math.toRadians(angle));

            Circle circle = new Circle(x, y, 20); // Créez un cercle de rayon 20 pour la ville

            // Ici, nous mettons à jour la couleur du cercle en fonction de l'état de la
            // zone de recharge de la ville
            Color color = ville.aZoneRecharge() ? Color.GREEN : Color.BLUE;
            circle.setFill(color);

            Label label = new Label(ville.getNom());
            label.setFont(new Font("Arial", 24)); // Taille de police plus grande pour une meilleure visibilité
            label.setTextFill(Color.RED); // Couleur de texte rouge pour les noms des villes
            label.setEffect(new DropShadow(1, Color.BLACK)); // Ombre portée pour améliorer la lisibilité

            // Centrez le texte dans le cercle
            label.layoutXProperty().bind(circle.centerXProperty().subtract(label.widthProperty().divide(2)));
            label.layoutYProperty().bind(circle.centerYProperty().subtract(label.heightProperty().divide(2).add(10)));

            graphPane.getChildren().addAll(circle, label);
            villeCircles.put(ville.getNom(), circle);
        }

        // Dessiner les routes à partir de routesFromFile
        for (Pair<String, String> routeInfo : CommunauteFileManager.getRoutesFromFile()) {
        String ville1Name = routeInfo.getKey();
        String ville2Name = routeInfo.getValue();
        Ville ville1 = communaute.getVille(ville1Name);
        Ville ville2 = communaute.getVille(ville2Name);

        if (ville1 != null && ville2 != null) {
            drawRoute(graphPane, ville1, ville2);
        }
    }
    }


}