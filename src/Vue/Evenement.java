package Vue;

import Constantes.Constantes;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Affiche un événement quel qu'il soit
 */
public class Evenement {

    // Champs
    private Text nom;
    private Text description;
    private Text departement;
    private ImageView journal;

    // Constructeur
    public Evenement(Modele.Evenement event, Modele.Jeu jeu) {
        journal = new ImageView(new Image("file:image\\PandemieJournal.jpg"));
        nom = new Text(event.getNom());
        nom.setFill(Color.WHITE);
        nom.setTextAlignment(TextAlignment.CENTER);
        description = new Text(event.getDescription());
        description.setFill(Color.WHITE);
        description.setTextAlignment(TextAlignment.CENTER);
        String classe = event.getClass().getSimpleName();
        String texte="";
        switch(classe) {
            case "Evenement":
                Modele.EvenementArticle modele = (Modele.EvenementArticle) event;
                texte = "Effets sur les départements : ";
                texte += "moral => " + modele.getMoral()+" / ";
                texte += "efficacite => " + modele.getEfficacite()+" / ";
                texte += "temps => " + modele.getTemps();
                break;
            case "EvenementAccomplissement":
                texte = "Le projet final avance petit a petit.";
                break;
        }
        departement = new Text(texte);
        departement.setFill(Color.WHITE);
        nom.setVisible(false);
        description.setVisible(false);
        departement.setVisible(false);
        journal.setVisible(false);
        jeu.getVue().getRoot().getChildren().addAll(journal, nom, description, departement);
    }

    // Méthodes
    /**
     * Affiche l'événement
     * @param jeu L'instance de jeu
     * @param afficher La valeur de switch (0 pour afficher, 1 pour enlever, 2 pour mettre à jour)
     */
    public void affichage(Modele.Jeu jeu, int afficher) {
    	Scene scene = jeu.getVue().getScene();
    	switch(afficher) {
            case 0:
                affichage(jeu, 2);
                nom.setVisible(true);
                description.setVisible(true);
                departement.setVisible(true);
                journal.setVisible(true);
                break;
            case 1:
                journal.setVisible(false);
                nom.setVisible(false);
                description.setVisible(false);
                departement.setVisible(false);
                break;
            default:
                description.setFont(Font.loadFont("file:cour.ttf", scene.getHeight() * Constantes.TAILLE_POLICE_EVENEMENT));
                nom.setFont(Font.loadFont("file:cour.ttf", scene.getHeight() * Constantes.TAILLE_POLICE_EVENEMENT_TITRE));
                departement.setFont(Font.loadFont("file:cour.ttf", scene.getHeight() * Constantes.TAILLE_POLICE_EVENEMENT));
                journal.setTranslateX(scene.getWidth() * Constantes.POS_X_JOURNAL);
                journal.setFitWidth(scene.getWidth() * Constantes.LARGEUR_ARTICLE);
                journal.setFitHeight(scene.getHeight() * Constantes.HAUTEUR_ARTICLE);
                nom.setX((int)(scene.getWidth() * Constantes.POS_X_EV_ART_NOM));
                nom.setY((int)(scene.getHeight() * Constantes.POS_Y_EV_ART_NOM));
                nom.setWrappingWidth(scene.getWidth() * Constantes.LARGEUR_ARTICLE_TEXTE_TITRE);
                description.setX((int)(scene.getWidth() * Constantes.POS_X_EV_ART_DESC));
                description.setY((int) (scene.getHeight() * Constantes.POS_Y_EV_ART_DESC));
                departement.setX((int) (scene.getWidth() * Constantes.POS_X_EV_ART_DEP));
                departement.setY((int) (scene.getHeight() * Constantes.POS_Y_EV_ART_DEP));
                description.setWrappingWidth(scene.getWidth() * Constantes.LARGEUR_ARTICLE_TEXTE_DESC);
        }
    }
}
