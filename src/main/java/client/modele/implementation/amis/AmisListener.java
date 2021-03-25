package client.modele.implementation.amis;

import client.controleur.app.amis.CtrlAmis;
import client.controleur.app.amis.CtrlDemandeAmis;
import client.controleur.app.amis.CtrlListeAmis;
import client.modele.serveur.stub.amis.chat.Message;
import client.modele.serveur.stub.connexion.joueur.JoueurProxy;
import client.modele.stub.amis.AmisListenerIF;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
