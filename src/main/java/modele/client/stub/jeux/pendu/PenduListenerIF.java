package modele.client.stub.jeux.pendu;

import modele.client.stub.jeux.JeuxListenerIF;

import java.rmi.RemoteException;

public interface PenduListenerIF extends JeuxListenerIF {

    /*
     * Permet de desactiver les boutons des lettres lorsqu'un joueur en propose une
     */
    public abstract void actualiserLettre(char lettre, boolean valide, String message) throws RemoteException;

    /*
     * Permet d'actualiser le mot lorsqu'une lettre est trouve
     */
    public abstract void actualiserMot(char[] motActualiser, boolean valide, String message) throws RemoteException;

    /*
     * Permet d'actualiser l'image du pendu en fonction du nombre d'erreur
     */
    public abstract void actualiserNbErreur(int nbErreur) throws RemoteException;

}
