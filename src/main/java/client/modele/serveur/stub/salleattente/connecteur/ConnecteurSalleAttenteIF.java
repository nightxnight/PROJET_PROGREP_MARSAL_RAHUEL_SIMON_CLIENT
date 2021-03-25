package client.modele.serveur.stub.salleattente.connecteur;

import client.modele.stub.salleattente.ListenerSalleAttenteIF;
import client.modele.serveur.stub.salleattente.SalleAttenteIF;
import client.modele.serveur.stub.salleattente.SalleAttenteProprietaireIF;
import client.modele.serveur.stub.salleattente.SalleAttenteProxy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ConnecteurSalleAttenteIF extends Remote {

    public abstract SalleAttenteProprietaireIF creer(String pseudoPropietaire, String nomSalle, String motDePasse, boolean publique) throws RemoteException, IllegalArgumentException, IllegalAccessException;
    public abstract SalleAttenteIF rejoindre(String pseudo, ListenerSalleAttenteIF clientListener, String nomSalle, String motDePasseSalle) throws RemoteException, IllegalArgumentException;
    public abstract ArrayList<SalleAttenteProxy> getListeSallesAttentes() throws RemoteException;
    public abstract ArrayList<SalleAttenteProxy> rechercher(String nomJeu, boolean masquerPleine, boolean masquerMdp) throws RemoteException;
}
