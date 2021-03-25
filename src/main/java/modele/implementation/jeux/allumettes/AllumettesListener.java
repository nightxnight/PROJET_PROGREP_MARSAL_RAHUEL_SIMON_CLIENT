package modele.implementation.jeux.allumettes;

import controleur.app.jeux.CtrlJeu;
import controleur.app.jeux.allumettes.CtrlAllumettes;
import modele.implementation.jeux.JeuxListener;
import modele.serveur.stub.jeux.application.ResultatPartieEnum;
import modele.client.stub.jeux.allumettes.AllumettesListenerIF;

import java.rmi.RemoteException;

public class AllumettesListener extends JeuxListener implements AllumettesListenerIF {

    private CtrlAllumettes controleur;

    public AllumettesListener() throws RemoteException { }

    @Override
    public void setControleur(CtrlJeu controleur) {
        this.controleur = (CtrlAllumettes) controleur;
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
    public void recupererParametres(int nbAllumettesInitial, int nbAllumettesMaxTour) throws RemoteException {
        controleur.recupererParametres(nbAllumettesInitial, nbAllumettesMaxTour);
    }

    @Override
    public void actualiserNbMaxAllumettesTour(int nombre) throws RemoteException {
        controleur.actualiserNombreMaxAllumettesTour(nombre);
    }

    @Override
    public void retirerAllumettes(String pseudo, int nbAllumettes, String message) throws RemoteException {
        controleur.retirerAllumettes(pseudo, nbAllumettes, message);
    }
}
