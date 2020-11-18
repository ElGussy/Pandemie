package Vue;

import javafx.scene.text.Text;

/**
 * Cette classe sert à instancier un compteur
 */
public class Compteur implements java.io.Serializable {

    // Champs
    protected int compte;
    protected int valeurMax;
    private transient Text text;

    // Constructeurs
    public Compteur(int c){
        compte = c;
        valeurMax = Integer.MAX_VALUE;
    }
    public Compteur(int c, int vMax){
        compte = c;
        valeurMax = vMax;
    }

    // Getters et setters
    public void setCompte(int c) { compte = c; }
    public int getCompte(){ return compte; }

    // Méthodes
    /**
     * Modifie la valeur du compteur
     * @param valeur La valeur à ajouter
     */
    public void modifCompte(int valeur){
        compte += valeur;
        if(compte < 0) compte = 0;
        else if(compte > valeurMax) compte = valeurMax;
    }
}
