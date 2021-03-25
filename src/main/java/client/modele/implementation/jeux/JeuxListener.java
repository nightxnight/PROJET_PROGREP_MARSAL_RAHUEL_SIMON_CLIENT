package client.modele.implementation.jeux;

import client.controleur.app.jeux.CtrlJeu;
import client.modele.implementation.jeux.allumettes.AllumettesListener;
import client.modele.implementation.jeux.morpion.MorpionListener;
import client.modele.implementation.jeux.pendu.PenduListener;
import client.modele.serveur.stub.jeux.application.JeuxEnum;
import client.modele.stub.jeux.JeuxListenerIF;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class JeuxListener extends UnicastRemoteObject implements JeuxListenerIF {

    public JeuxListener() throws RemoteException { }

    public abstract void setControleur(CtrlJeu controleur);

    public static JeuxListener creerFromJeu(JeuxEnum jeu) throws RemoteException {
        switch(jeu) {
            case MORPION: return new MorpionListener();
            case PENDU: return new PenduListener();
            case ALLUMETTES: return new AllumettesListener();
            default: throw new IllegalArgumentException("Should never occur");
        }
    }
}
