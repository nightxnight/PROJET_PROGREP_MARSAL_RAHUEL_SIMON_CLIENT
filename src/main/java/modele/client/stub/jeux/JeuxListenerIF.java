package modele.client.stub.jeux;

import modele.serveur.stub.jeux.application.ResultatPartieEnum;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JeuxListenerIF extends Remote {

    /*
     * Permet d'indiquer au controleur de jeu que le jeu a demarre
     */
    public abstract void lancerJeu() throws RemoteException;

    /*
     * Permet de faire jouer un controleur
     */
    public abstract void faireJouer() throws RemoteException;

    /*
     * Permet d'indiquer au controleur que la partie est termine
     */
    public abstract void arreterJeu(ResultatPartieEnum resultat, String message) throws RemoteException;

}
