package modele.client.stub.amis;

import modele.implementation.amis.chat.Message;
import modele.implementation.connexion.joueur.JoueurProxy;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AmisListenerIF extends Remote {

    /*
     * Fonction qui permet de recuperer un message prive d'un joueur: de
     */
    public abstract void recupererMessage(String de, Message message) throws RemoteException;

    /*
     * Fonction qui permet de recuperer un nouvel amis
     */
    public abstract void recupererAmis(JoueurProxy joueurProxy) throws RemoteException;

    /*
     * Fonction qui permet de supprimer un amis
     */
    public abstract void supprimerAmis(JoueurProxy joueurProxy) throws RemoteException;

    /*
     * Fonction qui permet de recuperer une demande d'amis
     */
    public abstract void recupererDemande(JoueurProxy joueurProxy) throws RemoteException;

    /*
     * Fonction qui permet d'actualiser l'etat d'un amis
     */
    public abstract void actualiserEtat(String pseudo, boolean etat) throws RemoteException;

}
