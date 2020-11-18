package Vue;

import Constantes.Constantes;
import Modele.Tache;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

/**
 * Partie Vue de la classe Departement
 * Affiche le département
 */
public class Departement {

    // Champs
    private Text information;
    private Text nomR;
    private Polygon departementPoly;
    private Group personne;
    private double posX;
    private double posY;
    private ArrayList<Double> polygone;
    private Modele.Departement modele;
    private ImagePattern pointInfecte;
    private ImagePattern pointNormalActif;
    private ImagePattern pointNormalInactif;
    private boolean affiche = false;

    // Constructeur
    public Departement(Modele.Departement modele, Modele.Jeu jeu){
        this.modele = modele;
        Scene scene = jeu.getVue().getScene();
        departementPoly = new Polygon();
        switch(modele.getNom()) {
            case "Informatique":
                posX = Constantes.POS_X_INFO;
                posY = Constantes.POS_Y_INFO;
                polygone = Constantes.POLYGONE_INFO;
                break;
            case "Energie":
                posX = Constantes.POS_X_ENERGIE;
                posY = Constantes.POS_Y_ENERGIE;
                polygone = Constantes.POLYGONE_ENERGIE;
                break;
            case "Imsi":
                posX = Constantes.POS_X_IMSI;
                posY = Constantes.POS_Y_IMSI;
                polygone = Constantes.POLYGONE_IMSI;
                break;
            case "Gmc":
                posX = Constantes.POS_X_GMC;
                posY = Constantes.POS_Y_GMC;
                polygone = Constantes.POLYGONE_GMC;
                break;
            default:
                posX = Constantes.POS_X_EDIM;
                posY = Constantes.POS_Y_EDIM;
                polygone = Constantes.POLYGONE_EDIM;
        }
        departementPoly.getPoints().addAll(Constantes.adaptPolygone(polygone, scene));
        departementPoly.setFill(new ImagePattern(new Image("file:image\\" + modele.getNom() + "Dep.jpg"), 0, 0, 1, 1, true));
        information = new Text();
        nomR = new Text();
        personne = new Group();
        pointInfecte = new ImagePattern(new Image("file:image\\PointInfecte.png"));
        pointNormalInactif = new ImagePattern(new Image("file:image\\PointNormalInactif.png"));
        pointNormalActif = new ImagePattern(new Image("file:image\\PointNormalActif.png"));

        personne.setOnMouseEntered(mouseEvent -> eventInformation(jeu));
        personne.setOnMouseExited(mouseEvent -> eventRemoveInformation(jeu));
        personne.setOnMouseClicked(mouseEvent -> eventArbreDeCompetence(jeu));
        personne.setVisible(false);
        information.setVisible(false);
        nomR.setVisible(false);
        jeu.getVue().getRoot().getChildren().addAll(information, nomR, personne);
    }

    // Getters et setters
    public Polygon getDepartementPoly() { return departementPoly; }

    // Méthodes
    /**
     * Affiche le département
     * @param jeu L'instance de jeu
     * @param afficher La valeur de switch (0 pour afficher, 1 pour enlever, 2 pour mettre à jour)
     */
    public void affichage(Modele.Jeu jeu, int afficher) {
        Scene scene = jeu.getVue().getScene();
        switch(afficher) {
            case 0:
                affiche = true;
                affichage(jeu, 2);
                personne.setVisible(true);
                break;
            case 1:
                affiche = false;
                personne.setVisible(false);
                information.setVisible(false);
                nomR.setVisible(false);
                break;
            default:
                personne.getChildren().clear();
                updateInformation(scene);
                departementPoly.getPoints().clear();
                departementPoly.getPoints().addAll(Constantes.adaptPolygone(polygone, scene));
                departementPoly.setTranslateX(jeu.getVue().getScene().getWidth() * posX);
                departementPoly.setTranslateY(jeu.getVue().getScene().getHeight() * posY);
                personne.getChildren().add(departementPoly);
                personne.getChildren().add(genePoint(jeu.getVue().getScene()));
                for(Tache t: modele.getTaches()) {
                    if (t.getEvent() != null) {

                        t.getEvent().getVue().affichage(jeu,2);

                    }
                }
        }
    }

    /**
     * Génère les points représentant les personnes à l'intérieur du département
     * @param scene L'instance de la scene
     * @return Un groupe d'éléments visuels
     */
    public Group genePoint(Scene scene) {
        Group depPersonne = new Group();
        for (int i = 0; i < modele.getNbPersonne(); i++) {
            Circle circle = new Circle();
            circle.setRadius(4);
            if (i < modele.getNbActif()) {
                circle.setFill(pointInfecte);
            } else if (modele.getNbTaches() > 0) {
                circle.setFill(pointNormalActif);
            } else {
                circle.setFill(pointNormalInactif);
            }
            do {
                circle.setCenterX(scene.getWidth() * posX + Math.random() * departementPoly.getLayoutBounds().getWidth());
                circle.setCenterY(scene.getHeight() * posY + Math.random() * departementPoly.getLayoutBounds().getHeight());
            } while(!departementPoly.contains(circle.getCenterX() - scene.getWidth() * posX, circle.getCenterY() - scene.getHeight() * posY));
            if(depPersonne.getChildren().contains(circle)) depPersonne.getChildren().remove(circle);
            depPersonne.getChildren().add(circle);
        }
        return depPersonne;
    }

    /**
     * Met à jour les informations au survol du département
     * @param scene L'instance de la scene
     */
    private void updateInformation(Scene scene) {
        String nom = modele.getNom();
        nomR.setText(nom);
        nomR.setX(scene.getWidth() * Constantes.POS_X_TEXTE);
        nomR.setY(scene.getHeight() * Constantes.POS_Y_TEXTE);
        nomR.setFont(Font.loadFont("file:Font.ttf", scene.getHeight() * Constantes.TAILLE_POLICE));
        int efficacite = modele.getEfficacite();
        int moral = modele.getMoral();
        int taches = modele.getNbTaches();
        int infecte = (int) ((modele.getNbActif() * 100.0) / modele.getNbPersonne());
        information.setText("Efficacité : " + Integer.toString(efficacite) + "%\nMoral : " + Integer.toString(moral) + "%\n" +
                "Nb tâches : " + Integer.toString(taches) + "\n" + "Nb actifs : " + Integer.toString(infecte) + "%");
        information.setX(scene.getWidth() * Constantes.POS_X_INFOS);
        information.setY(scene.getHeight() * Constantes.POS_Y_INFOS);
        information.setFont(Font.loadFont("file:Font.ttf", scene.getHeight() * Constantes.TAILLE_POLICE_DESC));
    }

    /**
     * Affiche les informations au survol du département
     * @param jeu L'instance de jeu
     */
    private void eventInformation(Modele.Jeu jeu){
        jeu.getVue().affichagePlateau(4);
        String nom = modele.getNom();
        departementPoly.setFill(new ImagePattern(new Image("file:image\\"+ nom +"DepSelec.jpg"), 0, 0, 1, 1, true));
        information.setVisible(true);
        nomR.setVisible(true);
    }

    /**
     * Enlève les informations du département lorsqu'il n'est plus survolé
     * @param jeu L'instance de jeu
     */
    private void eventRemoveInformation(Modele.Jeu jeu){
        String nom = modele.getNom();
        departementPoly.setFill(new ImagePattern(new Image("file:image\\" + nom + "Dep.jpg"), 0, 0, 1, 1, true));
        if (affiche) {
            information.setVisible(false);
            nomR.setVisible(false);
            jeu.getVue().affichagePlateau(2);
        }
    }

    /**
     * Affiche l'arbre de compétence du département
     * @param jeu L'instance de jeu
     */
    private void eventArbreDeCompetence(Modele.Jeu jeu){
        jeu.getVue().affichagePlateau(1);
        jeu.getVue().affichagePopUp(1);
        jeu.afficherCompte(1);
        modele.getArbre().getVue().affichage(jeu, 0);
        modele.afficherTaches(jeu.getVue().getScene(), 0);
    }
}
