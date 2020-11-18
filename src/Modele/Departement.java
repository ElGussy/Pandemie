package Modele;

import Constantes.Constantes;
import Enumerations.DepartementNom;
import Vue.Compteur;
import javafx.application.Platform;
import javafx.scene.Scene;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Partie Modele de la classe Departement
 * Cette classe sert à instancier un département
 */
public class Departement implements java.io.Serializable {

    // Champs
    private Enumerations.DepartementNom nom;
    private int nbPersonne;
    private List<Compteur> compteurs;
    private List<Modele.Tache> taches;
    private List<Modele.Tache> tachesStockage;
    private Modele.ArbreDeCompetence arbre;
    transient private Vue.Departement vue;

    // Constructeur
    public Departement(DepartementNom depNom, boolean depart, Modele.Jeu jeu){
        this.nom = depNom;
        this.nbPersonne = Constantes.NB_PERSONNE_MINI_DEP + (int)(Math.random() * Constantes.NB_PERSONNE_SUP_DEP);
        this.arbre = new ArbreDeCompetence(this, jeu);
        Compteur efficacite = new Compteur(Constantes.VALEUR_INIT_EFFICACITE, Constantes.VALEUR_MAX_EFFICACITE);
        Compteur moral = new Compteur(Constantes.VALEUR_INIT_MORAL, Constantes.VALEUR_MAX_MORAL);
        Compteur infecte = new Compteur(Constantes.VALEUR_INIT_INFECTES, nbPersonne);
        Compteur standBy = new Compteur(Constantes.VALEUR_INIT_STANDBY, nbPersonne);
        compteurs = new ArrayList<>(4);
        compteurs.add(efficacite);
        compteurs.add(moral);
        compteurs.add(infecte);
        compteurs.add(standBy);
        taches = new ArrayList<>();
        tachesStockage = new ArrayList<>();
        creeListeTache(jeu);
        if(depart) creerProjet(jeu);
        vue = new Vue.Departement(this, jeu);
    }

    // Méthodes
    /**
     * Recréé une vue (nécessaire car la vue n'est pas sérialisable)
     * @param jeu L'instance du jeu
     */
    public void creerVue(Modele.Jeu jeu){
        vue = new Vue.Departement(this, jeu);
        arbre.creerVue(jeu);
        for(Modele.Tache tache: taches) {
            tache.creerVue(jeu);
        }
    }

    /**
     * Getter de la vue
     * @return La vue
     */
    public Vue.Departement getVue() { return vue; }

    /**
     * Créé la tâche initiale dans le département de départ uniquement en la prenant dans le xml des projets
     * @param jeu L'instance de jeu
     */
    void creerProjet(Modele.Jeu jeu) {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document doc = builder.parse(new File(Constantes.PATH_PROJET));
            Element racine = doc.getDocumentElement();
            NodeList racineNoeuds = racine.getChildNodes();

            for (int i = 0; i < racineNoeuds.getLength(); i++) {
                if (racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE && racineNoeuds.item(i).getNodeName().equals(String.valueOf(this.nom))) {
                    NodeList difNoeuds = racineNoeuds.item(i).getChildNodes();
                    for (int k = 0; k < difNoeuds.getLength(); k++) {
                        if (difNoeuds.item(k).getNodeType() == Node.ELEMENT_NODE && difNoeuds.item(k).getNodeName().equals("projet")) {
                            Element elementTache = (Element) difNoeuds.item(k);
                            String nom = elementTache.getElementsByTagName("nom").item(0).getTextContent();
                            String description = elementTache.getElementsByTagName("description").item(0).getTextContent();
                            int temps = Integer.parseInt(elementTache.getElementsByTagName("temps").item(0).getTextContent());
                            int infectes = Integer.parseInt(elementTache.getElementsByTagName("infecte").item(0).getTextContent());

                            taches.add(new Tache(this,nom, description, new Compteur(temps), new Compteur(infectes), jeu));
                        }
                    }
                }
            }
        }
        catch (final ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Créé une simple tâche
     */
    void creerTache() {
        taches.add(tachesStockage.get((int)(Math.random() * tachesStockage.size())));
        compteurs.get(3).modifCompte(-taches.get(0).getInfectes());
        compteurs.get(2).modifCompte(taches.get(0).getInfectes());
    }

    /**
     * Propage l'infection dans le département en fonction de l'efficacité
     * @param jeu l'instance de jeu
     */
    void infection(Modele.Jeu jeu){
        int nbTaches = taches.size();
        int infection = nbTaches + (100-compteurs.get(1).getCompte())*4/100*nbTaches;
        compteurs.get(3).modifCompte(-infection);
        compteurs.get(2).modifCompte(infection);
        for(Tache t:taches){
            t.setInfectes(infection);
        }
    }

    /**
     * Affichage du département depuis la Timeline
     * @param jeu L'sintance de jeu
     */
    void affichage(Modele.Jeu jeu) {
        Platform.runLater(() -> vue.affichage(jeu, 2));
    }

    /**
     * Supprime les tâches terminées et génére les événements accomplissements
     * @param jeu L'instance de jeu
     */
    void supprimerTache(Modele.Jeu jeu) {
        Platform.runLater(() -> {
            for (int i = 0; i < taches.size(); i++) {
                if (taches.get(i).getTermine()) {
                    compteurs.get(3).modifCompte(taches.get(i).getInfectes());
                    compteurs.get(2).modifCompte(-taches.get(i).getInfectes());
                    jeu.ajoutEvenement(this, taches.get(i));
                    taches.remove(taches.get(i));
                }
            }
        });
    }

    /**
     * Affiche la liste des tâches
     * @param scene L'instance de la scene
     * @param afficher La valeur de switch (0 pour afficher, 1 pour enlever, 2 pour mettre à jour)
     */
    public void afficherTaches(Scene scene, int afficher) {
        switch(afficher) {
            case 0:
                for (int i = 0; i < taches.size(); i++) {
                    taches.get(i).getVue().affichage(scene, 0, i);
                }
                break;
            case 1:
                for (int i = 0; i < taches.size(); i++) {
                    taches.get(i).getVue().affichage(scene, 1, i);
                }
                break;
            default:
                for (int i = 0; i < taches.size(); i++) {
                    taches.get(i).getVue().affichage(scene, 2, i);
                }
        }
    }

    public int getEfficacite() { return compteurs.get(0).getCompte(); }
    public void setEfficacite(int ajout) { compteurs.get(0).modifCompte(ajout); }
    public int getMoral() { return compteurs.get(1).getCompte(); }
    public void setMoral(int ajout) { compteurs.get(1).modifCompte(ajout); }
    public String getNom(){ return String.valueOf(nom);}
    public ArbreDeCompetence getArbre(){ return arbre;}
    public int getNbTaches(){ return taches.size();}
    public int getNbActif(){ return compteurs.get(2).getCompte();}
    public int getNbPersonne() {return nbPersonne;}
    public List<Modele.Tache> getTaches() { return taches; }

    /**
     * Génère la liste des tâches et la stocke afin de ne pas avoir à réouvrir le xml à chaque fois
     * @param jeu L'instance de jeu
     */
    private void creeListeTache(Modele.Jeu jeu){
        int temps;
        int infectes;
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document doc = builder.parse(new File(Constantes.PATH_TACHES));
            Element racine = doc.getDocumentElement();
            NodeList racineNoeuds = racine.getChildNodes();

            for (int i = 0; i < racineNoeuds.getLength(); i++) {
                if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE && racineNoeuds.item(i).getNodeName().equals(String.valueOf(this.nom))) {
                    NodeList depNoeuds = racineNoeuds.item(i).getChildNodes();
                    for (int j = 0; j < depNoeuds.getLength(); j++) {
                        if(depNoeuds.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            NodeList difNoeuds = depNoeuds.item(j).getChildNodes();
                            for (int k = 0; k < difNoeuds.getLength(); k++) {
                                if(difNoeuds.item(k).getNodeType() == Node.ELEMENT_NODE && difNoeuds.item(k).getNodeName().equals("tache")) {
                                    Element elementTache = (Element) difNoeuds.item(k);
                                    String nom = elementTache.getElementsByTagName("nom").item(0).getTextContent();
                                    String description = elementTache.getElementsByTagName("description").item(0).getTextContent();
                                    temps = Integer.parseInt(elementTache.getElementsByTagName("temps").item(0).getTextContent());
                                    infectes = Integer.parseInt(elementTache.getElementsByTagName("infecte").item(0).getTextContent());

                                    tachesStockage.add(new Tache(this,nom, description, new Compteur(temps), new Compteur(infectes), jeu));
                                }
                            }
                        }
                    }
                }
            }
        }

        catch (final ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }
}
