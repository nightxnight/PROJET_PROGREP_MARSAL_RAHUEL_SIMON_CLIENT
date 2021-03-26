package controleur.app.amis;

import modele.implementation.connexion.joueur.JoueurProxy;

import java.util.ArrayList;

/*
 * Classe abstraite permettant de factoriser l'initialisation
 * des controleurs de la liste d'amis / liste demande d'amis
 */
public abstract class CtrlAmis {

    public abstract void initialiserListe(ArrayList<JoueurProxy> liste);

}
