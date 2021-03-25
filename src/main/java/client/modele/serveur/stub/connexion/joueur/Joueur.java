package client.modele.serveur.stub.connexion.joueur;

import java.io.Serializable;

public class Joueur implements Serializable {

    private String pseudo;
    private String mail;
    private String anniversaire;
    private String inscription;
    private String motDePasse;

    public boolean motDePasseEqual(String motDePasse) {
        return this.motDePasse.equals(motDePasse);
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getAnniversaire() {
        return anniversaire;
    }

    public String getMail() {
        return mail;
    }

    public String getInscription() {
        return inscription;
    }
}
