package controleur.app.amis;

import modele.serveur.stub.connexion.joueur.JoueurProxy;

import java.util.ArrayList;

public abstract class CtrlAmis {

    public abstract void initialiserListe(ArrayList<JoueurProxy> liste);

}
