package client.controleur.app.salleattente.parametresjeu;

import client.controleur.app.salleattente.CtrlSalleAttente;
import client.modele.serveur.stub.salleattente.SalleAttenteProprietaireIF;

import java.util.HashMap;

public abstract class CtrlParametresJeux {

    protected CtrlSalleAttente parent;

    public void initialiser(CtrlSalleAttente parent, HashMap<String, Object> parametres, SalleAttenteProprietaireIF droitsProprietaire) {
        this.parent = parent;
        for(String nom : parametres.keySet())
            changerParametre(nom, parametres.get(nom));
        mettreAJourComposants(droitsProprietaire);
    }

    public abstract void mettreAJourComposants(SalleAttenteProprietaireIF droitsProprietaire);
    public abstract void changerParametre(String nom, Object valeur) throws ClassCastException, IllegalArgumentException;
    public abstract HashMap<String, Object> getMapParametres();

}
