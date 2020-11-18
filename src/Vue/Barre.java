package Vue;


import Constantes.Constantes;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Classe fille de compteur qui affiche le compteur sous forme de barre de progression
 */
public class Barre extends Compteur {

    // Champs
    private Rectangle barre;
    private Rectangle progression;
    private Text texte;

    // Constructeur
    public Barre(int c, int vMax, Modele.Jeu jeu){
        super(c, vMax);
        texte = new Text("Avancement du projet :");
        barre = new Rectangle();
        barre.setFill(Color.DARKGRAY);
        progression = new Rectangle();
        progression.setFill(Color.ALICEBLUE);
        jeu.getVue().getRoot().getChildren().addAll(barre, progression, texte);
    }

    // Méthodes
    /**
     * Affiche la barre de progression
     * @param scene L'instance de scene
     * @param afficher La valeur de switch (0 pour afficher, 1 pour enlever, 2 pour mettre à jour)
     */
    public void affichage(Scene scene, int afficher) {
        switch(afficher) {
            case 0:
                affichage(scene, 2);
                barre.setVisible(true);
                progression.setVisible(true);
                texte.setVisible(true);
                break;
            case 1:
                barre.setVisible(false);
                progression.setVisible(false);
                texte.setVisible(false);
                break;
            default:
                barre.setX(scene.getWidth() * Constantes.POS_X_PROGRESSION);
                barre.setY(scene.getHeight() * Constantes.POS_Y_PROGRESSION);
                barre.setWidth(scene.getWidth() *  Constantes.LARGEUR_PROGRESSION);
                barre.setHeight(scene.getHeight() * Constantes.HAUTEUR_PROGRESSION);
                progression.setX(scene.getWidth() * Constantes.POS_X_PROGRESSION + 2);
                progression.setY(scene.getHeight() * Constantes.POS_Y_PROGRESSION + 2);
                progression.setWidth((double)(valeurMax - compte) / valeurMax * barre.getWidth());
                progression.setHeight(scene.getHeight() * Constantes.HAUTEUR_PROGRESSION - 4);
                texte.setX(scene.getWidth() * Constantes.POS_X_TEXTE_PROGRESSION);
                texte.setY(scene.getHeight() * Constantes.POS_Y_TEXTE_PROGRESSION);
                texte.setFont(Font.loadFont("file:Font.ttf", Constantes.TAILLE_POLICE * scene.getHeight()));
        }
    }
}
