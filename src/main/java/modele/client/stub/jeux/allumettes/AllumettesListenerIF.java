package modele.client.stub.jeux.allumettes;

import modele.client.stub.jeux.JeuxListenerIF;

import java.rmi.RemoteException;

public interface AllumettesListenerIF extends JeuxListenerIF {

    /*
     * Permet de recuperer les parametres du jeu
     */
    public abstract void recupererParametres(int nbAllumettesInitial, int nbAllumettesMaxTour) throws RemoteException;

    /*
     * Permet d'actualiser le nombre max d'allumettes retirable par tour
     */
    public abstract void actualiserNbMaxAllumettesTour(int nombre) throws RemoteException;

    /*
     * Permet de notifier qu'un joueur a retirer un nombre x d'allumettes
     */
    public abstract void retirerAllumettes(String pseudo, int nbAllumettes, String message) throws RemoteException;

}
