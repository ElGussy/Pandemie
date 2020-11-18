package Vue;

import Constantes.Constantes;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Classe fille de compteur qui affiche le d�compte de la date limite de rendu du projet
 */
public class Date extends Compteur implements java.io.Serializable {

    // Champs
    transient Text text;

    // Constructeur
    public Date(int c, Vue.Jeu jeu){
        super(c);
        text = new Text("");
        text.setVisible(false);
        jeu.getRoot().getChildren().add(text);
    }

    // M�thodes
    /**
     * Affiche le d�compte
     * @param scene L'instance de scene
     * @param afficher La valeur de switch (0 pour afficher, 1 pour enlever, 2 pour mettre � jour)
     */
    public void affichage(Scene scene, int afficher) {
        switch(afficher) {
            case 0:
                text.setVisible(true);
                affichage(scene, 2);
                break;
            case 1:
                text.setVisible(false);
                break;
            default:
                int weeks = compte/24/7;
                int days = compte/24 - weeks*7 ;
                int hours = compte - 24*(days + 7*weeks);
                if(weeks<10 && hours<10)text.setText("0" + weeks + ":0"+ days + ":0" + hours);
                else if(weeks<10)text.setText("0" + weeks + ":0"+ days + ":" + hours);
                else if(hours<10) text.setText("" + weeks + ":0"+ days + ":0" + hours);
                else text.setText("" + weeks + ":0"+ days + ":" + hours);
                text.setFont(Font.loadFont("file:DJB Get Digital.ttf", scene.getHeight() * Constantes.TAILLE_POLICE_DATE));
                text.setFill(Color.web("#3ab7e7"));
                text.setTranslateX(scene.getWidth() * Constantes.POS_X_DATE);
                text.setTranslateY(scene.getHeight() * Constantes.POS_Y_DATE);
        }
    }

    public void creerVue(Modele.Jeu jeu){
        text = new Text("");
        text.setVisible(false);
        jeu.getVue().getRoot().getChildren().add(text);
    }
}
