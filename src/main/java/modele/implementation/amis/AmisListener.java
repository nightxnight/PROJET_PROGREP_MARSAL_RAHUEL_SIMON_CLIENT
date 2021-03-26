package modele.implementation.amis;

import controleur.app.amis.CtrlAmis;
import controleur.app.amis.CtrlDemandeAmis;
import controleur.app.amis.CtrlListeAmis;
import modele.implementation.amis.chat.Message;
import modele.implementation.connexion.joueur.JoueurProxy;
import modele.client.stub.amis.AmisListenerIF;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/*
 * permet de faire le lien entre controleur et serveur
 */
public class AmisListener extends UnicastRemoteObject implements AmisListenerIF {

    public CtrlListeAmis controleurListeAmis;
    public CtrlDemandeAmis controleurDemandeAmis;

    public AmisListener() throws RemoteException {
        super();
    }

    public void setControleurs(CtrlAmis controleurListeAmis, CtrlAmis controleurDemandeAmis) {
        this.controleurListeAmis = (CtrlListeAmis) controleurListeAmis;
        this.controleurDemandeAmis = (CtrlDemandeAmis) controleurDemandeAmis;
    }

    @Override
    public void recupererMessage(String de, Message message) throws RemoteException {
        controleurListeAmis.recupererMessage(de, message);
    }

    @Override
    public void recupererAmis(JoueurProxy joueurProxy) throws RemoteException {
        controleurListeAmis.ajouterAmis(joueurProxy);
    }

    @Override
    public void supprimerAmis(JoueurProxy joueurProxy) throws RemoteException {
        controleurListeAmis.supprimerAmis(joueurProxy);
    }

    @Override
    public void recupererDemande(JoueurProxy joueurProxy) throws RemoteException {
        controleurDemandeAmis.ajouterDemande(joueurProxy);
    }

    @Override
    public void actualiserEtat(String pseudo, boolean etat) throws RemoteException {
        controleurListeAmis.actualiserEtat(pseudo, etat);
    }
}
