package Vue;

import Constantes.Constantes;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

/**
 * Classe qui sert à instancier l'accès au menu et le changement de mode de fenêtrage pendant le jeu
 */
public class Coin {

    // Champs
    private Image[] coinImagesStock;
    private Image[] coinImages;
    transient private ImageView coin;
    private Polygon polyMenu;
    private Polygon polyAffichage;

    // Constructeur
    public Coin(Modele.Jeu jeu) {
        Scene scene = jeu.getVue().getScene();
        Group root = jeu.getVue().getRoot();
        Stage primarystage = jeu.getVue().getPrimaryStage();
        coinImagesStock = new Image[]{ new Image("file:image\\FullScreen\\PandemieNoSelection.png"),
                new Image("file:image\\FullScreen\\PandemieMenuSelection.png"),
                new Image("file:image\\FullScreen\\PandemieAffichageSelection.png"),
                new Image("file:image\\Fenetre\\PandemieNoSelection.png"),
                new Image("file:image\\Fenetre\\PandemieMenuSelection.png"),
                new Image("file:image\\Fenetre\\PandemieAffichageSelection.png")};
        coinImages = new Image[]{coinImagesStock[3], coinImagesStock[4], coinImagesStock[5]};
        coin = new ImageView(coinImages[0]);
        coin.setX(0);
        coin.setY(0);
        polyMenu = new Polygon();
        polyMenu.getPoints().addAll(Constantes.adaptPolygone(Constantes.POLYGONE_COIN_MENU, scene));
        polyMenu.setFill(Color.TRANSPARENT);
        polyMenu.setOnMouseEntered(mouseEvent -> {
            coin.setImage(coinImages[1]);
            affichage(jeu, 2);
        });
        polyMenu.setOnMouseExited(mouseEvent -> {
            coin.setImage(coinImages[0]);
            affichage(jeu, 2);
        });
        polyMenu.setOnMouseClicked(mouseEvent -> {
            jeu.getVue().affichageMenuJeu(0);
            jeu.getRegles().affichage(jeu, 1);
            jeu.getVue().affichagePlateau(1);
            jeu.getVue().affichagePtsDeCompetence(1);
            jeu.getVue().affichageEvenement(1);
            jeu.getVue().affichagePopUp(1);
            jeu.afficherCompte(1);
        });
        polyAffichage = new Polygon();
        polyAffichage.getPoints().addAll(Constantes.adaptPolygone(Constantes.POLYGONE_COIN_AFFICHAGE, scene));
        polyAffichage.setFill(Color.TRANSPARENT);
        polyAffichage.setOnMouseEntered(mouseEvent -> {
            coin.setImage(coinImages[2]);
            affichage(jeu, 2);
        });
        polyAffichage.setOnMouseExited(mouseEvent -> {
            coin.setImage(coinImages[0]);
            affichage(jeu, 2);
        });
        polyAffichage.setOnMouseClicked(mouseEvent -> {
            if(jeu.getVue().getPrimaryStage().isFullScreen())
                primarystage.setFullScreen(false);
            else
                primarystage.setFullScreen(true);
        });
        coin.setVisible(false);
        root.getChildren().addAll(coin, polyAffichage, polyMenu);
    }

    /**
     * Affiche le menu coin
     * @param jeu L'instance de jeu
     * @param afficher La valeur de switch (0 pour afficher, 1 pour enlever, 2 pour mettre à jour)
     */
    public void affichage(Modele.Jeu jeu, int afficher) {
        Scene scene = jeu.getVue().getScene();
        switch(afficher) {
            case 0:
                coin.setVisible(true);
                affichage(jeu, 2);
                break;
            case 1:
                coin.setVisible(false);
                break;
            case 2:
                coin.setFitWidth(scene.getWidth() * Constantes.LARGEUR_COIN);
                coin.setFitHeight(scene.getHeight() * Constantes.HAUTEUR_COIN);
                break;
            default:
                if (jeu.getVue().getPrimaryStage().getX() == 0) {
                    polyMenu.getPoints().clear();
                    polyMenu.getPoints().addAll(Constantes.adaptPolygone(Constantes.POLYGONE_COIN_MENU, scene));
                    polyAffichage.getPoints().clear();
                    polyAffichage.getPoints().addAll(Constantes.adaptPolygone(Constantes.POLYGONE_COIN_AFFICHAGE, scene));
                    coinImages = new Image[]{coinImagesStock[3], coinImagesStock[4], coinImagesStock[5]};
                } else {
                    polyMenu.getPoints().clear();
                    polyMenu.getPoints().addAll(Constantes.POLYGONE_COIN_MENU);
                    polyAffichage.getPoints().clear();
                    polyAffichage.getPoints().addAll(Constantes.POLYGONE_COIN_AFFICHAGE);
                    coinImages = new Image[]{coinImagesStock[0], coinImagesStock[1], coinImagesStock[2]};
                    coin.setImage(coinImages[0]);
                }
                affichage(jeu, 2);
        }
    }
}
