package client.modele.serveur.stub.jeux.connecteur;

import client.modele.stub.jeux.JeuxListenerIF;
import client.modele.serveur.stub.jeux.application.JeuxIF;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConnecteurJeuxIF extends Remote {

    public abstract JeuxIF rejoindrePartie(String pseudo, JeuxListenerIF clientListener) throws RemoteException;

}
