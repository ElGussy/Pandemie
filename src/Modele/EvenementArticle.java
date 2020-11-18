package Modele;

import Constantes.Constantes;

/**
 * Cette classe sert à instancier un événement article.
 * Un événement article est généré aléatoirement et affecte les départements
 */
public class EvenementArticle extends Evenement implements java.io.Serializable {

	// Champs
	protected int effets[] = new int[Constantes.TAILLE_EFFETS]; // Contient le moral, efficacité et temps

	// Constructeurs
	public EvenementArticle(String nom, String description, int effets[], Modele.Jeu jeu) {
		super(nom, description, jeu);
		this.effets = effets;
		event = new Vue.Evenement(this, jeu);
	}

	public EvenementArticle(Modele.EvenementArticle evenement, Modele.Jeu jeu) {
		super(evenement.nom, evenement.description, jeu);
		effets = evenement.effets;
		event = new Vue.Evenement(this, jeu);
	}

	// Getters et setters
	public int getMoral() { return effets[0]; }
	public void setMoral(int moral) { this.effets[0] = moral; }
	public int getEfficacite() { return effets[1]; }
	public int getTemps() { return effets[2]; }
	public void setTemps(int temps) { this.effets[2] = temps; }

	// Méthodes
	/**
	 * Applique les effets de l'événement article
	 * @param jeu L'instance de jeu
	 */
	public void appliquerEffet(Modele.Jeu jeu) {
		for(Modele.Departement departement : jeu.getDepartements()) {
			departement.setMoral(effets[0]);
			departement.setEfficacite(effets[1]);
		}
		jeu.setTemps(effets[2]);
	}

	/**
	 * Recréé une vue (nécessaire car la vue n'est pas sérialisable)
	 * @param jeu L'instance de jeu
	 */
	public void creerVue(Modele.Jeu jeu){
		event = new Vue.Evenement(this, jeu);
	}
}
