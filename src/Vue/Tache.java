package Vue;

import Constantes.Constantes;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Partie Vue de la classe Tache
 * Affiche le nom de la tache
 */
public class Tache {

	// Champs
	private Text nom;

	// Constructeur
	public Tache(Modele.Tache modele, Modele.Jeu jeu) {
		nom = new Text("- " + modele.getNom());
		nom.setFill(Color.BLACK);
		nom.setVisible(false);
		jeu.getVue().getRoot().getChildren().add(nom);
	}

	// M�thodes
	/**
	 * Affiche le nom de la t�che � l'emplacement pr�vu � cet effet
	 * @param scene L'instance de la scene
	 * @param afficher La valeur de switch (0 pour afficher, 1 pour enlever, 2 pour mettre � jour)
	 * @param index L'index de la t�che, pour afficher la liste des t�ches en colonne
	 */
	public void affichage(Scene scene, int afficher, int index) {
		switch(afficher) {
			case 0:
				affichage(scene, 2, index);
				nom.setVisible(true);
				break;
			case 1:
				nom.setVisible(false);
				break;
			default:
				nom.setFont(Font.loadFont("file:Font.ttf", Constantes.TAILLE_POLICE_DESC * scene.getHeight()));
				nom.setX(scene.getWidth() * Constantes.POS_X_TACHE_NOM);
				nom.setY(scene.getHeight() * Constantes.POS_Y_TACHE_NOM + index * scene.getHeight() * Constantes.ESPACEMENT_LIGNES);
				nom.setWrappingWidth(scene.getWidth() * Constantes.LARGEUR_POST_IT);
		}
	}
}
