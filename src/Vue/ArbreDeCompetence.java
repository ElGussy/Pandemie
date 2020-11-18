package Vue;

import Constantes.Constantes;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Partie Vue de la classe ArbreDeCompetence
 * Affiche un arbre de comp�tence
 */
public class ArbreDeCompetence {

    // Champs
    private Modele.ArbreDeCompetence modele;
    private HashMap<String, Competence> competences;
    private Text nom;
    private String aCliquer;
    transient private ImageView retour;
    private Group lignes;
    private boolean screenIsShown;
    protected Text noPoint;

    // Constructeur
    public ArbreDeCompetence(Modele.ArbreDeCompetence modele, Modele.Jeu jeu){
        this.modele = modele;
        competences = new HashMap<>();
        nom = new Text(modele.getDepartement().getNom());
        aCliquer = "";
        retour = new ImageView(new Image("file:image\\RetourActif.jpg"));
        lignes = new Group();
        screenIsShown = false;
        noPoint = new Text("Vous n'avez pas assez de points");

        retour.setOnMouseEntered(mouseEvent -> retour.setImage(new Image("file:image\\Retour.jpg")));
        retour.setOnMouseExited(mouseEvent -> retour.setImage(new Image("file:image\\RetourActif.jpg")));
        noPoint.setFill(Color.ORANGERED);
        retour.setOnMouseClicked(event -> {
            this.affichage(jeu, 1);
            jeu.afficherCompte(0);
            jeu.retourJeu();
        });

        setVisibiltyComponents(false);
        setVisibiltyNoPoint(false);

        jeu.getVue().getRoot().getChildren().addAll(nom, retour, noPoint, lignes);
        for (Map.Entry<String, ArrayList<Modele.Competence>> competence : modele.getComp().entrySet()) {
            competences.put(competence.getKey(), new Competence(competence.getValue().get(0), this,jeu));
        }
    }

    // Getters et setters
    public String getACliquer() { return aCliquer; }
    public void setACliquer(String aCliquer) { this.aCliquer = aCliquer; }


    // M�thodes
    /**
     * Affiche l'arbre de competence
     * @param jeu L'instance de jeu
     * @param afficher La valeur de switch (0 pour afficher, 1 pour enlever, 2 pour mettre a jour)
     */
    void affichage(Modele.Jeu jeu, int afficher) {
        Scene scene = jeu.getVue().getScene();
        switch(afficher) {
            case 0:
                for (Map.Entry<String, Competence> comp : competences.entrySet()) {
                    comp.getValue().affichage(jeu, 0);
                }
                dimensionnement(scene);
                scene.setFill(new ImagePattern(new Image("file:image\\PandemieCompetence.jpg"), 0, 0, 1, 1, true));
                setVisibiltyComponents(true);
                break;
            case 1:
                for (Map.Entry<String, Competence> comp : competences.entrySet()) {
                    comp.getValue().affichage(jeu, 1);
                }
                modele.getDepartement().afficherTaches(jeu.getVue().getScene(),1);
                setVisibiltyComponents(false);
                setVisibiltyNoPoint(false);
                break;
            default:
                if(screenIsShown) {
                    dimensionnement(scene);
                    for (Map.Entry<String, Competence> comp : competences.entrySet()) {
                        comp.getValue().affichage(jeu, 2);
                    }
                }
        }
    }

    /**
     * Redimensionne l'arbre de competence
     * @param scene L'instance de la scene
     */
    void dimensionnement(Scene scene){
        nom.setX(scene.getWidth() * Constantes.POS_X_NOM_DEPARTEMENT_ARBRE);
        nom.setY(scene.getHeight() * Constantes.POS_Y_NOM_DEPARTEMENT_ARBRE);
        nom.setFont(Font.loadFont("file:Font.ttf", scene.getWidth() * Constantes.TAILLE_POLICE_TITRE));
        retour.setTranslateX(scene.getWidth() * Constantes.POS_X_FLECHEARBRE);
        retour.setTranslateY(scene.getHeight() * Constantes.POS_Y_FLECHEARBRE);
        retour.setFitWidth(scene.getWidth() * Constantes.LARGEUR_RETOUR);
        retour.setFitHeight(scene.getHeight() * Constantes.HAUTEUR_RETOUR);
        lignes.getChildren().clear();

        for (Map.Entry<String, ArrayList<Modele.Competence>> competence : modele.getComp().entrySet()) {
            if(competence.getValue().size() > 1) {
                for (int i = 1; i < competence.getValue().size(); ++i){
                    double coefx1 = (double) (competence.getValue().get(0).getLigne() - 1) / (competence.getValue().get(0).getNbLignes() - 1);
                    double coefx2 = (double) (competence.getValue().get(i).getLigne() - 1) / (competence.getValue().get(i).getNbLignes() - 1);
                    double coefy1 = calculateCoefY(competence.getValue().get(0));
                    double coefy2 = calculateCoefY(competence.getValue().get(i));

                    Line l = new Line();
                    l.setStartX(scene.getWidth() * Constantes.X_LIGNE_1 + coefx1 * scene.getWidth() * Constantes.X_LIGNE_2);
                    l.setStartY(scene.getHeight() * Constantes.Y_LIGNE_1 - coefy1 * scene.getHeight() * Constantes.Y_LIGNE_2);
                    l.setEndX(scene.getWidth() * Constantes.X_LIGNE_1  + coefx2 * scene.getWidth() * Constantes.X_LIGNE_2);
                    l.setEndY(scene.getHeight() * Constantes.Y_LIGNE_1 - coefy2 * scene.getHeight() * Constantes.Y_LIGNE_2);
                    lignes.getChildren().add(l);
                }
            }
        }
    }

    /**
     * Calcule le coefficient y pour relier des competence
     * @param competence competence utilise
     */
    private double calculateCoefY(Modele.Competence competence)
    {
        switch (competence.getNbColonnes())
        {
            case 2:
                if (competence.getColonne() == 1)
                    return 0.25d;
                else
                    return 0.75d;

            case 1:
                return 0.5d;

            default:
                return (double) (competence.getColonne() - 1) / (competence.getNbColonnes() - 1);
        }
    }

    /**
     * Change l'affichage d'une competence de bloquee a debloquee
     * @param ligne Ligne de la verification
     */
    void changementAffichage(int ligne){
        if(modele.getComp().get(ligne+1+","+1) != null) {
            for (int i = 1; i <= modele.getComp().get(ligne + 1 + "," + 1).get(0).getNbColonnes(); ++i) {
                if (competences.get((ligne + 1) + "," + (i)) != null) {
                    if (competences.get((ligne + 1) + "," + (i)).modele.getDebloque() && !competences.get((ligne + 1) + "," + (i)).modele.getAchete())
                        competences.get((ligne + 1) + "," + (i)).compet.setFill(new ImagePattern(new Image("file:image\\CompetenceDebloque.png"), 0, 0, 1, 1, true));
                }
            }
        }

    }

    /**
     * Affiche une erreur (pas assez de points de comp�tence ou comp�tence bloqu�e)
     * @param jeu L'instance de jeu
     * @param nom Le nom de l'erreur
     */
    public void erreurAchat(Modele.Jeu jeu, String nom) {
        noPoint.setText(nom);
        noPoint.setFont(Font.loadFont("file:Font.ttf", jeu.getVue().getScene().getHeight() * Constantes.TAILLE_POLICE));
        noPoint.setX((jeu.getVue().getScene().getWidth() * 83.5) / 100);
        noPoint.setY((jeu.getVue().getScene().getHeight() * 50) / 100);
        noPoint.setWrappingWidth((jeu.getVue().getScene().getWidth() * 14) / 100);
        noPoint.setVisible(true);
    }

    public void setVisibiltyNoPoint(boolean visible) {
        noPoint.setVisible(visible);
    }

    private void setVisibiltyComponents(boolean visible){
        nom.setVisible(visible);
        lignes.setVisible(visible);
        retour.setVisible(visible);
        screenIsShown = visible;
    }
}
