package modele.client.stub.jeux.morpion;

import modele.client.stub.jeux.JeuxListenerIF;

import java.rmi.RemoteException;

public interface MorpionListenerIF extends JeuxListenerIF {

    /*
     * Permet de recuperer les parametres du morpion
     */
    public abstract void recupererParametres(int taille, char symbole) throws RemoteException;

    /*
     * Permet de recuperer une case bloque par un joueur
     */
    public abstract void recupererCaseBloque(int ligne, int colonne, char symbole) throws RemoteException;

}
