package Modele;

/**
 * Cette classe sert � instancier un �v�nement textuel.
 * Un �v�nement textuel est g�n�r� au besoin et n'a aucun effet
 */
public class EvenementTextuel extends Evenement implements java.io.Serializable {

    // Constructeur
    public EvenementTextuel(String nom, String description, Modele.Jeu jeu) {
        super(nom, description, jeu);
        event = new Vue.Evenement(this, jeu);
    }
}
