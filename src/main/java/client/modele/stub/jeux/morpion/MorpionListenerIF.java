package client.modele.stub.jeux.morpion;

import client.modele.stub.jeux.JeuxListenerIF;

import java.rmi.RemoteException;

public interface MorpionListenerIF extends JeuxListenerIF {

    public abstract void recupererParametres(int taille, char symbole) throws RemoteException;
    public abstract void recupererCaseBloque(int ligne, int colonne, char symbole) throws RemoteException;

}
