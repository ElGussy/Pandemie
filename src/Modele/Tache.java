package Modele;

import Vue.Compteur;
import java.util.ArrayList;
import java.util.List;

/**
 * Partie Modele de la classe Tache
 * Cette classe sert à instancier une tâche dans un département
 */
public class Tache implements java.io.Serializable {

	// Champs
	private final String description;
	private String nom;
	private List<Compteur> compteurs;
	private int tempsInitial;
	private Modele.EvenementAccomplissement event;
	private Modele.Departement departement;
	transient private Vue.Tache vue;
	private boolean termine;

	// Constructeur
	public Tache(Modele.Departement departement, String nom, String description, Compteur temps, Compteur infectes, Modele.Jeu jeu) {
		this.departement = departement;
		this.nom = nom;
		this.description = description;
		this.compteurs = new ArrayList<>();
		this.compteurs.add(temps);
		this.compteurs.add(infectes);
		tempsInitial = temps.getCompte();
		termine = false;
		vue = new Vue.Tache(this, jeu);
	}

	// Getters et setters
	public Vue.Tache getVue() { return vue; }
	public void setAvancement(int incr) {
		compteurs.get(0).modifCompte(incr);
		if(this.getAvancement() == 0) termine = true;
	}
	public void setAvancement() {
		compteurs.get(0).modifCompte(-departement.getEfficacite() / 5);
		if(this.getAvancement() == 0) termine = true;
	}
	public void setInfectes(int incr) {
		compteurs.get(1).modifCompte(incr);
	}
    public String getNom() { return nom; }
	public String getDescription() { return description; }
	public int getAvancement() { return compteurs.get(0).getCompte(); }
	public int getInfectes() { return compteurs.get(1).getCompte(); }
	public int getTempsInitial() {
		return tempsInitial;
	}
	public boolean getTermine() { return termine; }
	public Modele.EvenementAccomplissement getEvent() { return event;}
	public Modele.Departement getDepartement() { return departement; }

	// Méthodes
	/**
	 * Recréé une vue (nécessaire car la vue n'est pas sérialisable)
	 * @param jeu L'instance de jeu
	 */
	public void creerVue(Modele.Jeu jeu) {
		vue = new Vue.Tache(this, jeu);
	}
}