package controleur.app.salleattente.parametresjeu;

import controleur.app.salleattente.CtrlSalleAttente;
import modele.serveur.stub.salleattente.SalleAttenteProprietaireIF;

import java.util.HashMap;

public abstract class CtrlParametresJeux {

    protected CtrlSalleAttente parent;

    public void initialiser(CtrlSalleAttente parent, HashMap<String, Object> parametres, SalleAttenteProprietaireIF droitsProprietaire) {
        this.parent = parent;
        for(String nom : parametres.keySet())
            changerParametre(nom, parametres.get(nom));
        mettreAJourComposants(droitsProprietaire);
    }

    /*
     * Permet de changer la valeur des composants si une modification est detecter sur le serveur
     */
    public abstract void mettreAJourComposants(SalleAttenteProprietaireIF droitsProprietaire);
    /*
     * Permet de changer un parametre de nom: nom et de valeur: valeur
     * On utilise une valeur de Type Object, pour pouvoir passer des booleens, des entiers, des Strings, des JeuxEnum
     */
    public abstract void changerParametre(String nom, Object valeur) throws ClassCastException, IllegalArgumentException;
    /*
     * Permet de recuperer la valeur des composants (parametres) ranger dans une HashMap
     */
    public abstract HashMap<String, Object> getMapParametres();

}
