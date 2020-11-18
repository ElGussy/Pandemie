package Modele;

import Constantes.Constantes;

/**
 * Classe qui va faire progresser "l'infection" et générer les événements aléatoires
 */
public class Timeline extends Thread {

    // Champs
    private Modele.Jeu jeu;
    private boolean end;
    private boolean partie = true;
    private boolean ecranFin = true;
    public static int vitesse;

    // Constructeur
    public Timeline(String nom, Modele.Jeu jeu) {
        super(nom);
        this.jeu = jeu;
        vitesse = 1000;
        this.start();
    }

    // Getters et setters
    public void setEnd(boolean end) {
        this.end = end;
    }

    // Méthodes
    /**
     * Méthode principale du thread qui gère toutes les boucles pseudo-infinies du jeu
     */
    public void run() {
        while(!end) {
            while(partie && !end) {
                pause();
                if (jeu.getDepartements().size() == 5 && !jeu.getMenuJeu().getAffiche()) {
                    jeu.afficherAvancement(2);
                    jeu.getVue().affichagePtsDeCompetence(2);
                    jeu.setTemps(-1);

                    int depComplet = 0;
                    for (Modele.Departement departement : jeu.getDepartements()) {
                        if (departement.getNbActif() == departement.getNbPersonne()) depComplet++;
                        if (departement.getTaches().size() > 0) {
                            departement.infection(jeu);
                        }
                        if (jeu.getDepartements().get(0).getTaches().get(0).getAvancement() == 0) {
                            partie = false;
                            jeu.getVue().afficherVictoire(0, 0);
                        } else {
                            departement.supprimerTache(jeu);
                        }
                        departement.affichage(jeu);
                        for (Modele.Tache tache : departement.getTaches()) {
                            tache.setAvancement();
                        }
                    }
                    if (depComplet == 5 || jeu.getTemps() == 0) {
                        partie = false;
                        jeu.getVue().afficherGameOver(0, 3);
                    }
                    jeu.afficherCompte(2);
                    if (jeu.getPopUps().size() != 0) {
                        jeu.getVue().affichagePopUp(2);
                    }
                    if ((int) (Math.random() * 15) == 0) {
                        jeu.ajoutPopUp();
                    }
                    if ((int) (Math.random() * 22) == 7) {
                        jeu.perdreMoral();
                    }
                    if (jeu.getEvenements().size() == 0) {
                        if (jeu.getEvenements().size() == 0 && (int) (Math.random() * 10) == 0) {
                            if ((int) (Math.random() * 2) == 0) {
                                int depAlea = (int) (Math.random() * jeu.getDepartements().size());
                                Modele.Departement departement = jeu.getDepartements().get(depAlea);
                                if (departement.getTaches().size() < 4) {
                                    departement.creerTache();
                                    Modele.Tache tache = departement.getTaches().get(departement.getTaches().size() - 1);
                                    String nom = tache.getNom();
                                    jeu.ajoutEvenement("Vous avez demandé de l'aide au département " + departement.getNom() + " :", nom);
                                }
                            } else {
                                jeu.ajoutEvenement();
                            }
                        }
                    }
                    jeu.getVue().affichageEvenement(2);
                }
            }
            int i = 0;
            int j = 0;
            while(ecranFin && !end) {
                pause();
                jeu.getVue().afficherVictoire(2, i);
                jeu.getVue().afficherGameOver(2, 3 + j);
                i = (i + 1)%3;
                j = (j + 1)%2;
            }
        }
    }

    /**
     * Met en pause le thread
     */
    private void pause() {
        try {
            Modele.Timeline.sleep(vitesse);
        } catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
