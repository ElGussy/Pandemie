package Modele;

/**
 * Cette classe sert à instancier un événement textuel.
 * Un événement textuel est généré au besoin et n'a aucun effet
 */
public class EvenementTextuel extends Evenement implements java.io.Serializable {

    // Constructeur
    public EvenementTextuel(String nom, String description, Modele.Jeu jeu) {
        super(nom, description, jeu);
        event = new Vue.Evenement(this, jeu);
    }
}
