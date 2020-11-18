package Modele;

/**
 * Cette classe sert à instancier un événement accomplissement.
 * Un événement accomplissement sera généré à chaque fois qu'une tâche se termine
 */
public class EvenementAccomplissement extends Evenement implements java.io.Serializable {

    // Champs
	private int point; // Calculé en fonction de la tâche

    // Constructeur
    public EvenementAccomplissement(Modele.Departement departement, Modele.Tache tache, Modele.Jeu jeu){
        super("Tâche terminée !","Une tâche a été terminée dans le département : "+departement.getNom(),jeu);
        point = (int)(Math.random()*(tache.getTempsInitial()/100))+1;
        appliquerEffet(jeu,tache);
        event = new Vue.Evenement(this,jeu);
    }

    // Méthodes
    /**
     * Applique les effets de l'accomplissement
     * @param jeu L'instance de jeu
     * @param tache La tâche dont est issu l'événement
     */
    public void appliquerEffet(Modele.Jeu jeu, Modele.Tache tache){
        jeu.setPtsCompetence(point);
        jeu.getDepartements().get(0).getTaches().get(0).setAvancement(-tache.getTempsInitial() / 4);
        tache.getDepartement().setMoral(+10);
    }
}
