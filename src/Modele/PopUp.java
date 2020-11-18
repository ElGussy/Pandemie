package Modele;

/**
 * Partie Modele de la classe Pop Up
 * Cette classe sert � instancier un Pop Up qui appara�t pour une dur�e limit�e et donne des points de comp�tence si cliqu�
 */
public class PopUp {

    // M�thodes
    private int duree;
    private int point;
    transient private Vue.PopUp vue;
    private Modele.Departement departement;

    // Constructeur
    public PopUp(Modele.Departement depart, Modele.Jeu jeu){
        departement = depart;
        duree = 10;
        point = (int)(Math.random()*2)+1;
        vue = new Vue.PopUp(this,jeu);
    }

    // Getters et setters
    public Vue.PopUp getVue() { return vue;}
    public void setDuree() {
        --duree;
        if(duree < 0) duree = 0;
    }
    public int getDuree() { return duree; }
    public Modele.Departement getDepartement() { return departement;}

    // M�thodes
    /**
     * Applique les effets du pop up
     * @param jeu L'instance de jeu
     */
    public void appliquerEffet(Modele.Jeu jeu){ jeu.setPtsCompetence(point); }
}
