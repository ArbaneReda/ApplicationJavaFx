import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.text.Font;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.scene.Node;

/**
 * Cette classe est la deuxième page de l'application, elle contient les
 * méthodes pour afficher les villes et les routes, et pour gérer les actions de
 * l'utilisateur.
 */
public class SecondPage {

    private final Stage primaryStage;
    private final Scene mainScene;
    private final CommunauteAgglomeration communaute;
    private Map<String, Circle> villeCircles;
    private GestionFichier fileManager;
    private double initialTranslateX;
    private double initialTranslateY;
    private double initialMouseX;
    private double initialMouseY;

    /**
     * Constructeur de la classe SecondPage.
     * 
     * @param primaryStage : la fenêtre principale de l'application.
     * @param mainScene    : la scène principale de l'application.
     * @param communaute   : la communauté d'agglomération.
     */
    public SecondPage(Stage primaryStage, Scene mainScene, CommunauteAgglomeration communaute) {
        this.primaryStage = primaryStage;
        this.mainScene = mainScene;
        this.communaute = communaute;
        this.villeCircles = new HashMap<>();
        this.fileManager = new GestionFichier(communaute);
    }

    /**
     * Méthode pour afficher la deuxième page de l'application.
     * 
     * @param : void
     * @return : void
     */
    public void show() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(15));
        borderPane.setStyle("-fx-background-color: #252323;");

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

    /**
     * Méthode pour créer un Pane avec un style CSS personnalisé.
     * @param width
     * @param height
     * @return Pane
     */
    private Pane createStyledPane(double width, double height) {
        Pane pane = new Pane();
        pane.setMinSize(width, height);
        pane.setStyle("-fx-background-color: #A89B9D; -fx-border-color: #cccccc; -fx-border-width: 1;");
        pane.setEffect(new DropShadow(5, Color.VIOLET));

        // Ajout de la fonctionnalité de zoom
        pane.setOnScroll(event -> {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY < 0) {
                zoomFactor = 1 / zoomFactor;
            }
            pane.setScaleX(pane.getScaleX() * zoomFactor);
            pane.setScaleY(pane.getScaleY() * zoomFactor);
            event.consume();
        });

        // Ajout de la fonctionnalité de glissement (panning)
        pane.setOnMousePressed(event -> {
            // Coordonnées initiales pour le déplacement
            initialTranslateX = pane.getTranslateX();
            initialTranslateY = pane.getTranslateY();
            initialMouseX = event.getSceneX();
            initialMouseY = event.getSceneY();
        });

        pane.setOnMouseDragged(event -> {
            // Déplacer le Pane en fonction de la différence de position de la souris
            pane.setTranslateX(initialTranslateX + event.getSceneX() - initialMouseX);
            pane.setTranslateY(initialTranslateY + event.getSceneY() - initialMouseY);
        });

        return pane;
    }

    /**
     * Méthode pour créer un TextArea avec un style CSS personnalisé.
     * @param : void
     * @return : TextArea
     */
    private TextArea createTextArea() {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle(
                "-fx-font-size: 14px; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #FAFAFA;");
        textArea.setEffect(new DropShadow(5, Color.GREY));
        return textArea;
    }

    /**
     * Méthode pour afficher les villes et les routes dans le Pane.
     * @param text
     * @param color
     * @return Button
     */
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

    /**
     * Gère le bouton de démarrage.
     * @param graphPane
     * @param terminalOutputPane
     * @param buttonBox
     * @return void
     */
    private void handleStart(Pane graphPane, TextArea terminalOutputPane, HBox buttonBox) {
        buttonBox.getChildren().clear(); // Nettoyer les boutons existants

        Button manuelButton = createButton("Manuellement", "#4CAF50");
        Button fichierButton = createButton("Fichier", "#2196F3");

        manuelButton.setOnAction(e -> saisieManuelle(graphPane, terminalOutputPane, buttonBox));
        fichierButton.setOnAction(e -> {
            File selectedFile = this.fileManager.ouvrirFichierCommunaute(primaryStage);
            if (selectedFile != null) {
                showRoutesMenu(buttonBox, graphPane, terminalOutputPane, true);
                displayGraph(graphPane, terminalOutputPane);
            } else {
                // Si aucun fichier n'est sélectionné, afficher une alerte ou simplement ne rien
                // faire
                showAlert("Aucun fichier sélectionné", "Aucun fichier n'a été sélectionné.");
            }
        });

        buttonBox.getChildren().addAll(manuelButton, fichierButton);
    }

    /**
     * Calcule le rayon optimal pour afficher les cercles des villes.
     * @param numberOfCities
     * @param graphPane
     * @return double
     */
    private double calculateOptimalRadius(int numberOfCities, Pane graphPane) {
        double baseRadius = graphPane.getWidth() / 4; // Utilisez un quart de la largeur du Pane comme base
        double radiusIncrement = 20; // L'augmentation du rayon pour chaque ville supplémentaire
        double maxRadius = Math.min(graphPane.getWidth(), graphPane.getHeight()) / 2 - 50; // Limite maximale
        double dynamicRadius = baseRadius + (numberOfCities - 1) * radiusIncrement;
        return Math.min(dynamicRadius, maxRadius);
    }

    /**
     * Affiche le menu de saisie manuelle.
     * @param graphPane
     * @param terminalOutputPane
     * @param buttonBox
     * @return void
     */
    private void saisieManuelle(Pane graphPane, TextArea terminalOutputPane, HBox buttonBox) {
        TextInputDialog numberDialog = new TextInputDialog();
        numberDialog.setTitle("Nombre de villes");
        numberDialog.setHeaderText("Création des villes");
        numberDialog.setContentText("Entrez le nombre de villes :");

        Optional<String> numberResult = numberDialog.showAndWait();
        numberResult.ifPresent(number -> {
            try {
                int nbVilles = Integer.parseInt(number);
                ajouterVillesAutomatiquement(communaute, nbVilles); // Utilisez la méthode pour ajouter automatiquement
                                                                    // les villes
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

    /**
     * Ajoute automatiquement des villes à la communauté d'agglomération.
     * @param communaute
     * @param nombreVilles
     * @return void
     */
    private void ajouterVillesAutomatiquement(CommunauteAgglomeration communaute, int nombreVilles) {
        for (int i = 0; i < nombreVilles; i++) {
            char lettre = (char) ('A' + (i % 26));
            int indice = (i / 26) + 1;
            String nomVille = lettre + String.valueOf(indice);
            Ville ville = new Ville(nomVille);
            communaute.ajouterVille(ville);
        }
    }

    /**
     * Affiche le menu des routes.
     * @param buttonBox
     * @param graphPane
     * @param terminalOutputPane
     * @param clearGraph
     * @return void
     */
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

    /**
     * Affiche le menu des zones de recharge.
     * @param graphPane
     * @param terminalOutputPane
     * @param buttonBox
     * @return void
     */
    private void showRechargeZoneMenu(Pane graphPane, TextArea terminalOutputPane, HBox buttonBox) {
        // Nettoyer l'interface utilisateur actuelle
        buttonBox.getChildren().clear();
        terminalOutputPane.clear();

        // boutons pour les actions de la zone de recharge
        Button addRechargeZoneButton = createButton("Ajouter une zone de recharge", "#4CAF50");
        Button removeRechargeZoneButton = createButton("Supprimer une zone de recharge", "#F44336");
        Button optimizeButton = createButton("Optimiser les zones de recharge", "#FF5722");
        Button backButton = createButton("Retour", "#6c7ae0");

        addRechargeZoneButton.setOnAction(e -> addRechargeZone(graphPane));

        removeRechargeZoneButton.setOnAction(e -> removeRechargeZone(graphPane));

        backButton.setOnAction(e -> showRoutesMenu(buttonBox, graphPane, terminalOutputPane, false));

        optimizeButton.setOnAction(e -> {
            // Assurez-vous que la méthode algorithmeOptimiser est statique ou créez une
            // instance de la classe Algorithme*
            Algorithmes.algorithmeOptimiser(communaute); // Appel de la méthode d'optimisation depuis la classe
                                                         // Algorithme
            updateVilleCircles();
            terminalOutputPane.appendText("Optimisation terminée. Nombre de zones de recharge: "
                    + communaute.getZonesRecharge().size() + "\n"); // Supposant que vous avez une méthode pour obtenir

            showSaveQuitMenu(graphPane, terminalOutputPane, buttonBox);// les zones
        });

        // Ajouter les nouveaux boutons à la HBox
        buttonBox.getChildren().addAll(addRechargeZoneButton, removeRechargeZoneButton, optimizeButton, backButton);
    }

    /**
     * Met à jour les cercles pour refléter les changements des zones de recharge.
     * @param : void
     * @return : void
     */
    private void updateVilleCircles() {
        // Met à jour les cercles pour refléter les changements des zones de recharge
        for (Ville ville : communaute.getVilles()) {
            Circle circle = villeCircles.get(ville.getNom());
            if (circle != null) {
                circle.setFill(ville.aZoneRecharge() ? Color.GREEN : Color.BLUE);
            }
        }
    }

    /**
     * Affiche le menu de sauvegarde et de quitter.
     * @param graphPane
     * @param terminalOutputPane
     * @param buttonBox
     * @return void
     */
    private void showSaveQuitMenu(Pane graphPane, TextArea terminalOutputPane, HBox buttonBox) {

        buttonBox.getChildren().clear();
        terminalOutputPane.clear();

        Button saveButton = createButton("Sauvegarder", "#4CAF50");
        Button quitButton = createButton("Quitter", "#F44336");

        saveButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Community Data");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showSaveDialog(primaryStage);

            if (selectedFile != null) {
                GestionFichier gestionFichier = new GestionFichier(communaute); // Assuming GestionFichier has a no-arg
                                                                                // constructor
                gestionFichier.sauvegarderCommunaute(selectedFile.getAbsolutePath());
                showAlert("Sauvegarde réussie",
                        "Les données ont été sauvegardées avec succès dans : " + selectedFile.getAbsolutePath());
            }
        });

        quitButton.setOnAction(e -> {
            Platform.exit();
        });

        buttonBox.getChildren().addAll(saveButton, quitButton);
    }

    /**
     * Méthode pour ajouter une route.
     * @param graphPane
     * @return void
     */
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

    /**
     * Méthode pour supprimer une route.
     * @param graphPane
     * @return void
     */
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

    /**
     * Met à jour le graphique en supprimant la ligne correspondant à la route
     * supprimée.
     * @param graphPane
     * @param ville1
     * @param ville2
     * @return void
     */
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

    /**
     * Méthode pour ajouter une zone de recharge.
     * @param graphPane
     * @return void
     */
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

    /**
     * Méthode pour supprimer une zone de recharge.
     * @param graphPane
     * @return void
     */
    private void removeRechargeZone(Pane graphPane) {
        List<Ville> villes = communaute.getVilles();
        ChoiceDialog<Ville> dialog = new ChoiceDialog<>(villes.get(0), villes);
        dialog.setTitle("Supprimer une zone de recharge");
        dialog.setHeaderText("Sélectionnez une ville:");
        Optional<Ville> result = dialog.showAndWait();

        // si une ville n'a pas de voisin, c'est à dire qu'elle est isolée, on ne peut
        // pas supprimer sa zone de recharge

        result.ifPresent(ville -> { // ifPresent() est appelé si l'utilisateur a sélectionné une ville
            if (!ville.aZoneRecharge()) {
                showAlert("Suppression de zone de recharge",
                        "La ville " + ville.getNom() + " n'a pas de zone de recharge à supprimer.");
                return;
            }
            // si une ville n'a pas de voisin, c'est à dire qu'elle est isolée, on ne peut
            // pas supprimer sa zone de recharge
            if (communaute.getVillesAdjacentes(ville).isEmpty()) {
                showAlert("Suppression de zone de recharge",
                        "Vous ne pouvez pas supprimer la zone de recharge de " + ville.getNom() +
                                " car la ville est isolée.");
                return;
            }

            // On vérifie si la suppression de la zone de recharge de 'ville' isole une
            // ville voisine
            for (Ville voisine : communaute.getVillesAdjacentes(ville)) {
                // Si 'voisine' n'a pas de zone de recharge et que toutes les villes adjacentes
                // à 'voisine', sauf 'ville',
                // n'ont pas de zone de recharge, alors on ne peut pas supprimer la zone de
                // recharge de 'ville'
                if (!voisine.aZoneRecharge() && communaute.getVillesAdjacentes(voisine).stream() // .stream() est
                                                                                                 // utilisé pour
                                                                                                 // convertir la liste
                                                                                                 // en flux
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

    /**
     * Met à jour la couleur du cercle d'une ville.
     * @param ville
     * @param color
     * @return void
     */
    private void updateCircleColor(Ville ville, Color color) {
        Circle circle = villeCircles.get(ville.getNom());
        if (circle != null) {
            circle.setFill(color);
        }
    }

    /**
     * Affiche une alerte avec un titre et un contenu.
     * @param title
     * @param content
     * @return void
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Dessine une route entre deux villes.
     * @param graphPane
     * @param ville1
     * @param ville2
     * @return void
     */
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

    /**
     * Affiche les villes et les routes dans le Pane.
     * @param graphPane de type Pane 
     * @param terminalOutputPane
     */
    private void displayGraph(Pane graphPane, TextArea terminalOutputPane) {

        graphPane.getChildren().removeIf(node -> node instanceof Circle || node instanceof Label);

        List<Ville> villes = communaute.getVilles();

        double radius = calculateOptimalRadius(villes.size(), graphPane);
        double centerX = graphPane.getWidth() / 2;
        double centerY = graphPane.getHeight() / 2;
        double angleStep = 360.0 / villes.size();

        for (int i = 0; i < villes.size(); i++) {
            Ville ville = villes.get(i);
            double angle = Math.toRadians(-90.0 + i * angleStep); // Convertissez en radians pour le calcul

            // Déplacez cette ligne avant son premier usage dans les calculs de 'x' et 'y'
            double circleRadius = Math.max(10, 30 - 0.5 * villes.size());

            // Calculez les positions 'x' et 'y' en vous assurant qu'elles sont dans les
            // limites du Pane
            double x = Math.max(circleRadius,
                    Math.min(centerX + radius * Math.cos(angle), graphPane.getWidth() - circleRadius));
            double y = Math.max(circleRadius,
                    Math.min(centerY + radius * Math.sin(angle), graphPane.getHeight() - circleRadius));

            Circle circle = new Circle(x, y, circleRadius);
            circle.setFill(ville.aZoneRecharge() ? Color.GREEN : Color.BLUE);

            int fontSize = (int) Math.max(10, 24 - 0.2 * villes.size());
            Label label = new Label(ville.getNom());
            label.setFont(new Font("Arial", fontSize));
            label.setTextFill(Color.RED);
            label.setEffect(new DropShadow(1, Color.BLACK));

            label.layoutXProperty().bind(circle.centerXProperty().subtract(label.widthProperty().divide(2)));
            label.layoutYProperty().bind(circle.centerYProperty().subtract(label.heightProperty().divide(2)));

            graphPane.getChildren().addAll(circle, label);
            villeCircles.put(ville.getNom(), circle);
        }
        // Dessiner les routes
        drawRoutes(graphPane);
    }

    /**
     * Dessine les routes entre les villes.
     * @param graphPane
     * @return void
     */
    private void drawRoutes(Pane graphPane) {
        for (Pair<String, String> routeInfo : GestionFichier.getRoutesFromFile()) {
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