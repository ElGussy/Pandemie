package Vue;

import Modele.*;
import Modele.PopUp;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import Constantes.Constantes;

import java.util.ArrayList;

/**
 * Partie Vue de la classe Jeu
 * Cette classe sert à instancier l'affichage du jeu
 */

public class Jeu {

    // Champs
    private Modele.Jeu modele;
    private static Group root;
    private static Scene scene;
    transient private ImageView liste;
    private Text texte;
    private Stage primaryStage;
    private boolean affiche = false;
    private Text nbPoints;
    private ImageView vitesse1;
    private ImageView vitesse2;
    private ImageView vitesse3;
    private ImageView victoire;
    private ImageView defaite;
    private Image[] finImagesStock;

    // Constructeur
    public Jeu(Stage primaryStage, Modele.Jeu modele) {
        this.primaryStage = primaryStage;
        this.modele = modele;
        root = new Group();
        scene = new Scene(root, Constantes.LARGEUR_FENETRE, Constantes.HAUTEUR_FENETRE);
        liste = new ImageView(new Image("file:image\\Liste.jpg"));
        texte = new Text("Départements");
        nbPoints = new Text("Nombre de points : : 0");
        nbPoints.setVisible(false);
        liste.setVisible(false);
        texte.setVisible(false);

        finImagesStock = new Image[]{ new Image("file:image\\PandemieVictory1.jpg"),
                new Image("file:image\\PandemieVictory2.jpg"),
                new Image("file:image\\PandemieVictory3.jpg"),
                new Image("file:image\\PandemieGameOver1.jpg"),
                new Image("file:image\\PandemieGameOver2.jpg")};
        victoire = new ImageView();
        defaite = new ImageView();
        victoire.setVisible(false);
        defaite.setVisible(false);

        vitesse1 = new ImageView(new Image("file:image\\FlecheP1.jpg"));
        vitesse1.setOnMouseClicked(event -> Timeline.vitesse = 1000);
        vitesse1.setOnMouseEntered(event -> vitesse1.setTranslateX(scene.getWidth() * (Constantes.POS_X_FLECHE1_VITESSE + 0.001)));
        vitesse1.setOnMouseExited(event -> vitesse1.setTranslateX(scene.getWidth() * Constantes.POS_X_FLECHE1_VITESSE));

        vitesse2 = new ImageView(new Image("file:image\\Vitesse2.png"));
        vitesse2.setOnMouseClicked(event -> Timeline.vitesse = 750);
        vitesse2.setOnMouseEntered(event -> vitesse2.setTranslateX(scene.getWidth() * (Constantes.POS_X_FLECHE2_VITESSE + 0.001)));
        vitesse2.setOnMouseExited(event -> vitesse2.setTranslateX(scene.getWidth() * Constantes.POS_X_FLECHE2_VITESSE));

        vitesse3 = new ImageView(new Image("file:image\\Vitesse3.png"));
        vitesse3.setOnMouseClicked(event -> Timeline.vitesse = 500);
        vitesse3.setOnMouseEntered(event -> vitesse3.setTranslateX(scene.getWidth() * (Constantes.POS_X_FLECHE3_VITESSE + 0.001)));
        vitesse3.setOnMouseExited(event -> vitesse3.setTranslateX(scene.getWidth() * Constantes.POS_X_FLECHE3_VITESSE));


        vitesse1.setVisible(false);
        vitesse2.setVisible(false);
        vitesse3.setVisible(false);
        nbPoints.setVisible(false);
        root.getChildren().addAll(liste, texte, nbPoints, vitesse1, vitesse2, vitesse3, victoire, defaite);

        primaryStage.setTitle("Study Project Simulator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(true);
        primaryStage.show();
        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> modele.redimensionner());
        scene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> modele.redimensionner());
    }

    // Getters et setters
    public Group getRoot() { return root; }
    public Scene getScene() { return scene; }
    public Stage getPrimaryStage() { return primaryStage; }
    public boolean getAffiche() { return affiche; }

    /**
     * Méthode qui affichePlateau le "plateau de jeu"
     * @param afficher Entier qui détermine l'action à effectuer. 0 pour afficher, 1 pour désafficher,
     *                 2 pour afficher uniquement les informations, pas les départements, 3 pour mettre à jour l'affichage
     *                 et 4 pour enlever uniquement les informations, pas les départements
     */
    public void  affichagePlateau(int afficher) {
        switch(afficher) {
            case 0:
                affiche = true;
                scene.setFill(new ImagePattern(new Image("file:image\\PandemieDep.jpg"), 0, 0, 1, 1, true));
                for(Modele.Departement departement : modele.getDepartements()) {
                    departement.getVue().affichage(modele, 0);
                }
                affichagePlateau(3);
                modele.afficherAvancement(0);
                modele.afficherAvancement(0);
                vitesse1.setVisible(true);
                vitesse2.setVisible(true);
                vitesse3.setVisible(true);
                liste.setVisible(true);
                texte.setVisible(true);
                nbPoints.setVisible(true);
                modele.getCoin().affichage(modele, 0);
                break;
            case 1:
                affiche = false;
                for(Modele.Departement departement : modele.getDepartements()) {
                    departement.getVue().affichage(modele, 1);
                    departement.getArbre().getVue().affichage(modele, 1);
                }
                modele.afficherAvancement(1);
                vitesse1.setVisible(false);
                vitesse2.setVisible(false);
                vitesse3.setVisible(false);
                affichagePlateau(4);
                break;
            case 2:
                modele.afficherAvancement(2);
                liste.setVisible(true);
                texte.setVisible(true);
                break;
            case 3:
                modele.getCoin().affichage(modele, 3);
                for(Modele.Departement departement : modele.getDepartements()) {
                    departement.getVue().affichage(modele, 2);
                }
                for(Modele.Departement departement : modele.getDepartements()) {
                    departement.getArbre().getVue().affichage(modele,2);
                }
                liste.setX(scene.getWidth() * Constantes.POS_X_LISTE);
                liste.setY(scene.getHeight() * Constantes.POS_Y_LISTE);
                liste.setFitWidth(scene.getWidth() * Constantes.LARGEUR_LISTE);
                liste.setFitHeight(scene.getHeight() * Constantes.HAUTEUR_LISTE);
                texte.setX(scene.getWidth() * Constantes.POS_X_TEXTE);
                texte.setY(scene.getHeight() * Constantes.POS_Y_TEXTE);
                texte.setFont(Font.loadFont("file:Font.ttf", scene.getHeight() * Constantes.TAILLE_POLICE));

                vitesse1.setTranslateX(scene.getWidth() * Constantes.POS_X_FLECHE1_VITESSE);
                vitesse1.setTranslateY(scene.getHeight() * Constantes.POS_Y_FLECHE_VITESSE);
                vitesse1.setFitWidth(scene.getWidth() * Constantes.WIDTH_FLECHE1_VITESSE);
                vitesse1.setFitHeight(scene.getHeight() * Constantes.HEIGHT_FLECHE_VITESSE);

                vitesse2.setTranslateX(scene.getWidth() * Constantes.POS_X_FLECHE2_VITESSE);
                vitesse2.setTranslateY(scene.getHeight() * Constantes.POS_Y_FLECHE_VITESSE);
                vitesse2.setFitWidth(scene.getWidth() * Constantes.WIDTH_FLECHE2_VITESSE);
                vitesse2.setFitHeight(scene.getHeight() * Constantes.HEIGHT_FLECHE_VITESSE);

                vitesse3.setTranslateX(scene.getWidth() * Constantes.POS_X_FLECHE3_VITESSE);
                vitesse3.setTranslateY(scene.getHeight() * Constantes.POS_Y_FLECHE_VITESSE);
                vitesse3.setFitWidth(scene.getWidth() * Constantes.WIDTH_FLECHE3_VITESSE);
                vitesse3.setFitHeight(scene.getHeight() * Constantes.HEIGHT_FLECHE_VITESSE);

                nbPoints.setX(scene.getWidth() * Constantes.POS_X_PTC);
                nbPoints.setY(scene.getHeight() * Constantes.POS_Y_PTC);
                nbPoints.setFill(new Color(0.305,0.305,0.305,1));
                nbPoints.setFont(Font.loadFont("file:Font.ttf", scene.getHeight() * Constantes.TAILLE_POLICE));
                break;
            default:
                liste.setVisible(false);
                texte.setVisible(false);
        }
    }

    /**
     * Affiche le menu en jeu
     * @param afficher La valeur de switch (0 pour afficher, 1 pour enlever)
     */
    public void affichageMenuJeu(int afficher) {
        switch(afficher) {
            case 0:
                modele.getMenuJeu().affichage(modele, 0);
                break;
            default:
                modele.getMenuJeu().affichage(modele, 1);
        }
    }

    /**
     * Affiche les pop ups
     * @param afficher La valeur de switch (0 pour afficher, 1 pour enlever, 2 pour mettre à jour)
     */
    public void affichagePopUp(int afficher) {
        ArrayList<Modele.PopUp> popUps = modele.getPopUps();
        switch(afficher) {
            case 0:
                for(Modele.PopUp popUp : popUps) {
                    popUp.getVue().affichage(modele, 0);
                }
                break;
            case 1:
                for(Modele.PopUp popUp : popUps) {
                    popUp.getVue().affichage(modele, 1);
                }
                break;
            default:
                Platform.runLater(() -> {
                    for (PopUp popUp : popUps) {
                        popUp.getVue().affichage(modele, 2);
                        popUp.setDuree();
                    }
                    modele.enleverPopUp();
                });
        }
    }

    /**
     * Affiche les événements
     * @param afficher La valeur de switch (0 pour afficher, 1 pour enlever, 2 pour mettre à jour)
     */
    public void affichageEvenement(int afficher) {
        switch(afficher) {
            case 0:
                for(Modele.Evenement evenement : modele.getEvenements()) {
                    evenement.getVue().affichage(modele, 0);
                }
                break;
            case 1:
                for(Modele.Evenement evenement : modele.getEvenements()) {
                    evenement.getVue().affichage(modele, 1);
                }
                break;
            default:
                if (modele.getEvenements().size() > 0) {
                    Platform.runLater(() -> {
                        if (modele.getEvenements().get(0).getDuree() == 0) modele.getEvenements().remove(0);
                    });
                    modele.getEvenements().get(0).diminuerTemps(modele);
                }
        }
    }

    /**
     * Affiche le compteur des points de compétence
     * @param affichage La valeur de switch (1 pour enlever, 2 pour mettre à jour)
     */
    public void affichagePtsDeCompetence(int affichage) {

        Platform.runLater(() -> {
            switch (affichage) {
                case 1:
                    nbPoints.setVisible(false);
                    affichagePlateau(1);
                    break;
                default:
                    nbPoints.setText("Nombre de points : " + modele.getPtsCompetence());
                    break;
            }
        });
    }

    /**
     * Affiche l'écran de victoire
     * @param afficher La valeur de switch (0 pour afficher, 2 pour mettre à jour)
     * @param index L'index de l'image de l'animation
     */
    public void afficherVictoire(int afficher, int index) {
        Platform.runLater(() -> {
            switch(afficher) {
                case 0:
                    affichageEvenement(1);
                    modele.getCoin().affichage(modele, 1);
                    modele.afficherCompte(1);
                    affichagePlateau(1);
                    victoire.setVisible(true);
                    break;
                case 2:
                    victoire.setImage(finImagesStock[index]);
                    victoire.setFitWidth(scene.getWidth());
                    victoire.setFitHeight(scene.getHeight());
                    break;
            }
        });
    }

    /**
     * Affiche l'écran de Game Over
     * @param afficher La valeur de switch (0 pour afficher, 2 pour mettre à jour)
     * @param index L'index de l'image de l'animation
     */
    public void afficherGameOver(int afficher, int index) {
        Platform.runLater(() -> {
            switch(afficher) {
                case 0:
                    affichageEvenement(1);
                    modele.getCoin().affichage(modele, 1);
                    modele.afficherCompte(1);
                    affichagePlateau(1);
                    defaite.setVisible(true);
                    break;
                case 2:
                    defaite.setImage(finImagesStock[index]);
                    defaite.setFitWidth(scene.getWidth());
                    defaite.setFitHeight(scene.getHeight());
                    break;
            }
        });
    }
}
