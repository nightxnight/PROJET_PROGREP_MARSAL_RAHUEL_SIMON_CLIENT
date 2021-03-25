package modele.serveur.stub.salleattente;

import java.io.Serializable;

public class SalleAttenteProxy implements Serializable {

    private String nomSalle;
    private String nomJeu;
    private int nbJoueursConnecte;
    private int nbJoueursMax;
    private boolean besoinMdp;
    private boolean publique;

    public String getNomSalle() {
        return nomSalle;
    }

    public String getNomJeu() {
        return nomJeu;
    }

    public int getNbJoueursConnecte() {
        return nbJoueursConnecte;
    }

    public int getNbJoueursMax() {
        return nbJoueursMax;
    }

    public boolean isBesoinMdp() {
        return besoinMdp;
    }

    public boolean isPublique() {
        return publique;
    }
}
