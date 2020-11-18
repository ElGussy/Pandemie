package Modele;

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
import java.util.HashMap;

/**
 * Partie Modele de la classe ArbreDeCompetence
 * Cette classe sert à instancier un arbre regroupant des compétences pour un département donné
 */
public class ArbreDeCompetence implements java.io.Serializable {

    // Champs
    private HashMap<String, ArrayList<Competence>> competencesMod;
    private Departement departement;
    transient private Vue.ArbreDeCompetence vue;

    // Constructeur
    public ArbreDeCompetence(Departement departement, Modele.Jeu jeu){
        this.departement = departement;
        competencesMod = new HashMap<>();
        initialiserArbre();
        vue = new Vue.ArbreDeCompetence(this, jeu);
    }

    // Getters et setters
    public Departement getDepartement() { return departement; }
    public Vue.ArbreDeCompetence getVue() { return vue; }
    public HashMap<String, ArrayList<Competence>> getComp(){ return competencesMod; }

    // Méthodes
    /**
     * Débloque une compétence donnée
     * @param ligne Ligne de la compétence
     * @param colonne Colonne de la compétence
     */
    public void debloquerCompetence(int ligne, int colonne){

        competencesMod.get(ligne+","+colonne).get(0).setAchete();
        competencesMod.get(ligne+","+colonne).get(0).applicationCompetenceDepartement();
        if(competencesMod.get(ligne+1+","+1) != null) {
            for (int i = 1; i <= competencesMod.get(ligne + 1 + "," + 1).get(0).getNbColonnes(); ++i) {

                boolean debloque = true;
                ArrayList<Competence> c = competencesMod.get(ligne + 1 + "," + i);
                if (c != null) {

                    for (int j = 1; j < c.size(); ++j) {

                        if (!c.get(j).debloque || !c.get(j).achete) debloque = false;

                    }
                    if (debloque) {

                        c.get(0).setDebloque();

                    }

                }

            }
        }
    }

    /**
     * Recréé une vue (nécessaire car la vue n'est pas sérialisable)
     * @param jeu L'instance de jeu
     */
    public void creerVue(Modele.Jeu jeu){
        vue = new Vue.ArbreDeCompetence(this, jeu);
    }

    /**
     * Initialise l'arbre de compétence en récupérant les éléments des compétences dans un fichier xml
     */
    private void initialiserArbre() {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document doc = builder.parse(new File("xml\\competence.xml"));
            Element racine = doc.getDocumentElement(); // Récupère l'élément "compétences"

            NodeList racineNoeuds = racine.getChildNodes(); // Récupère tous les sous-éléments de "compétences"
            int nbRacineNoeuds = racineNoeuds.getLength();
            boolean initialiser = false;

            for (int i = 0; i<nbRacineNoeuds && !initialiser; ++i) {
                // Ne prend que la partie qui nous intéresse càd les compétences du type du département
                if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE && racineNoeuds.item(i).getNodeName().equals(departement.getNom())) {

                    Element type = (Element) racineNoeuds.item(i);

                    NodeList lignes = type.getElementsByTagName("lignes"); // Récupère tous les éléments "lignes"
                    Element lignesE = (Element) lignes.item(0);

                    NodeList colonnes = lignesE.getElementsByTagName("colonne");
                    int nbLignes = colonnes.getLength();

                    NodeList competences = type.getElementsByTagName("competence"); // Récupère tous les éléments "compétence"
                    int nbCompetences = competences.getLength();

                    for (int j = 0; j<nbCompetences; ++j) {
                        Element elementComp = (Element) competences.item(j); // Récupère les infos de la compétence qui nous intéresse
                        String nom = elementComp.getElementsByTagName("nom").item(0).getTextContent();
                        String description = elementComp.getElementsByTagName("description").item(0).getTextContent();
                        int ligne = Integer.parseInt(elementComp.getElementsByTagName("ligne").item(0).getTextContent());
                        int colone = Integer.parseInt(elementComp.getElementsByTagName("colonne").item(0).getTextContent());
                        int[] effet = new int[3];
                        effet[0] = Integer.parseInt(elementComp.getElementsByTagName("efficacite").item(0).getTextContent());
                        effet[1] = Integer.parseInt(elementComp.getElementsByTagName("moral").item(0).getTextContent());
                        effet[2] = Integer.parseInt(elementComp.getElementsByTagName("temps").item(0).getTextContent());
                        int cout = Integer.parseInt(elementComp.getElementsByTagName("cout").item(0).getTextContent());
                        NodeList sommets = elementComp.getElementsByTagName("sommetLie"); // Récupère les infos sur les sommets liés
                        Element so = (Element) sommets.item(0);
                        String sommetLie = "";
                        NodeList sommetL = so.getElementsByTagName("sommet");
                        int nbSommetL = sommetL.getLength();

                        for (int k = 0; k < nbSommetL; ++k) {
                            sommetLie += sommetL.item(k).getTextContent();
                            sommetLie += ";";
                        }

                        Competence c = new Competence(nom,description,ligne,colone,effet,cout,sommetLie, Integer.parseInt(colonnes.item(ligne-1).getTextContent()),nbLignes,this);
                        competencesMod.put(c.ligne + "," + c.colonne, new ArrayList<>());
                        competencesMod.get(c.ligne + ","+c.colonne).add(c);

                        if(c.sommetLie != null) {
                            String sommet[] = c.sommetLie.split(";");
                            for (String s : sommet) {
                                competencesMod.get(c.ligne + ","+c.colonne).add(competencesMod.get(s).get(0));
                            }
                        }
                    }
                    initialiser = true;
                }

            }

        }
        catch (final ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
