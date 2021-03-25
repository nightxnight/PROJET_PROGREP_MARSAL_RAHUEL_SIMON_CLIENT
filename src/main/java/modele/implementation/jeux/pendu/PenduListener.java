package modele.implementation.jeux.pendu;

import controleur.app.jeux.CtrlJeu;
import controleur.app.jeux.pendu.CtrlPendu;
import modele.implementation.jeux.JeuxListener;
import modele.serveur.stub.jeux.application.ResultatPartieEnum;
import modele.client.stub.jeux.pendu.PenduListenerIF;

import java.rmi.RemoteException;

public class PenduListener extends JeuxListener implements PenduListenerIF {

    /*
     * Controleur associe afin de mettre a jour l'IG
     */
    private CtrlPendu controleur;

    public PenduListener() throws RemoteException {
        super();
    }

    @Override
    public void setControleur(CtrlJeu controleur) {
        this.controleur = (CtrlPendu) controleur;
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
    public void actualiserLettre(char lettre, boolean valide, String message) throws RemoteException {
        controleur.recupererLettrePropose(lettre, valide, message);
    }

    @Override
    public void actualiserMot(char[] motActualiser, boolean valide, String message) throws RemoteException {
        controleur.recupererMot(motActualiser, valide, message);
    }

    @Override
    public void actualiserNbErreur(int nbErreur) throws RemoteException {
        controleur.actualiserErreur(nbErreur);
    }
}
