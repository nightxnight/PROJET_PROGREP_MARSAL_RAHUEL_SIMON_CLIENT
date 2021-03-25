package client.modele.implementation.salleattente;

import client.controleur.app.salleattente.CtrlSalleAttente;
import client.modele.serveur.stub.connexion.joueur.JoueurProxy;
import client.modele.serveur.stub.jeux.application.JeuxEnum;
import client.modele.serveur.stub.jeux.connecteur.ConnecteurJeuxIF;
import client.modele.serveur.stub.salleattente.SalleAttenteProprietaireIF;
import client.modele.stub.salleattente.ListenerSalleAttenteIF;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ListenerSalleAttente extends UnicastRemoteObject implements ListenerSalleAttenteIF {

    private CtrlSalleAttente controleur;

    public void setControleur(CtrlSalleAttente controleur) {
        this.controleur = controleur;
    }

    public ListenerSalleAttente() throws RemoteException {
        super();
    }

    @Override
    public void connexionPartie(ConnecteurJeuxIF connecteur, JeuxEnum jeu) throws RemoteException {
        controleur.rejoindrePartie(connecteur, jeu);
    }

    @Override
    public void designerProprietaire(SalleAttenteProprietaireIF droitProprietaire) throws RemoteException {
        controleur.designerProprietaire(droitProprietaire);
    }

    @Override
    public void changerParametreSalle(String nomParametre, Object valeur) throws RemoteException {
        controleur.changerParametresSalle(nomParametre, valeur);
    }

    @Override
    public void changerParametreJeu(String nomParametre, Object valeur) throws RemoteException {
        controleur.changerParametresJeu(nomParametre, valeur);
    }

    @Override
    public void exclusion() throws RemoteException {
        controleur.exclure();
    }

    @Override
    public void actualiserJoueur() throws RemoteException {
        controleur.actualiserJoueur();
    }

    @Override
    public void recupererMessage(String expediteur, String message) throws RemoteException {
        controleur.afficherMessage(expediteur + ": " + message);
    }

    @Override
    public void connexionJoueur(JoueurProxy joueur) {
        controleur.connexionJoueur(joueur);
    }

    @Override
    public void deconnexionJoueur(String pseudo) {
        controleur.deconnexionJoueur(pseudo);
    }

    @Override
    public void changerEtatJoueur(String pseudo, boolean etat) throws RemoteException {
        controleur.changementEtat(pseudo, etat);
    }

    @Override
    public void activerPret(boolean active) throws RemoteException {
        controleur.activerPret(active);
    }
}
