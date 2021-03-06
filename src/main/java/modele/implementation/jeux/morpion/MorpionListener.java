package modele.implementation.jeux.morpion;

import controleur.app.jeux.CtrlJeu;
import controleur.app.jeux.morpion.CtrlMorpion;
import modele.implementation.jeux.JeuxListener;
import modele.serveur.stub.jeux.application.ResultatPartieEnum;
import modele.client.stub.jeux.morpion.MorpionListenerIF;

import java.rmi.RemoteException;

/*
 * permet de faire le lien entre controleur et serveur
 */
public class MorpionListener extends JeuxListener implements MorpionListenerIF {

    private CtrlMorpion controleur;

    public MorpionListener() throws RemoteException {
        super();
    }

    @Override
    public void setControleur(CtrlJeu controleur) {
        this.controleur = (CtrlMorpion) controleur;
    }

    @Override
    public void lancerJeu() throws RemoteException {
        controleur.lancerPartie();
    }

    @Override
    public void faireJouer() throws RemoteException {
        controleur.faireJouer();
    }

    @Override
    public void arreterJeu(ResultatPartieEnum resultat, String message) throws RemoteException {
        controleur.arreterJeu(resultat, message);
    }

    @Override
    public void recupererParametres(int taille, char symbole) throws RemoteException {
        controleur.recupererParametres(taille, symbole);
    }

    @Override
    public void recupererCaseBloque(int ligne, int colonne, char symbole) throws RemoteException {
        controleur.recupererCaseBloque(ligne, colonne, symbole);
    }
}
