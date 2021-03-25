package modele.serveur.stub.connexion.joueur;

import java.io.Serializable;

public class JoueurProxy implements Serializable {

    private String pseudo;
    private boolean enLigne;

    public String getPseudo() {
        return pseudo;
    }
    public boolean isEnLigne() {
        return enLigne;
    }
}
