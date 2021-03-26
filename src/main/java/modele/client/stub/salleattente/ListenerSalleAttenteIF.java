package modele.client.stub.salleattente;

import modele.implementation.connexion.joueur.JoueurProxy;
import modele.serveur.stub.jeux.application.JeuxEnum;
import modele.serveur.stub.jeux.connecteur.ConnecteurJeuxIF;
import modele.serveur.stub.salleattente.SalleAttenteProprietaireIF;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ListenerSalleAttenteIF extends Remote {

    /*
     * Permet d'indiquer au controleur qu'il faut ouvrir un panel de jeu
     */
    public abstract void connexionPartie(ConnecteurJeuxIF connecteur, JeuxEnum jeu) throws RemoteException;

    /*
     * permet d'indiquer au controleur qu'il est le nouveau proprietaire
     */
    public abstract void designerProprietaire(SalleAttenteProprietaireIF droitProprietaire) throws RemoteException;

    /*
     * Permet d'indiquer au controleur un changement de parametre de salle
     */
    public abstract void changerParametreSalle(String nomParametre, Object valeur) throws RemoteException;
    /*
     * De meme pour les parametres de jeux
     */
    public abstract void changerParametreJeu(String nomParametre, Object valeur) throws RemoteException;

    /*
     * Permet d'indiquer au client qu'il s'est fait exclure
     */
    public abstract void exclusion() throws RemoteException;

    /*
     * Permet au client d'actualiser le nombre de joueur dans la salle
     */
    public abstract  void actualiserJoueur() throws RemoteException ;
    /*
     * Permet au client de recuperer un message dans le chat de salle d'attente
     */
    public abstract void recupererMessage(String expediteur, String message) throws RemoteException;

    /*
     * Permet au serveur de notifier la connexion d'un nouveau joueur dans la salle d'attente
     */
    public abstract void connexionJoueur(JoueurProxy joueur) throws RemoteException;
    /*
     * De meme pour la deconnexion
     */
    public abstract void deconnexionJoueur(String pseudo) throws RemoteException;

    /*
     * permet au serveur d'indiquer aux clients si un joueur a changer son etat
     */
    public abstract void changerEtatJoueur(String pseudo, boolean etat) throws RemoteException;

    /*
     * Permet au serveur d'indiquer aux controleurs qu'il faut desactiver le bouton pret si le
     * nombre de joueur n'est pas valide.
     */
    public abstract void activerPret(boolean active) throws RemoteException;
}
