package controleur.app.jeux;

import controleur.app.CtrlPrincipal;
import modele.implementation.jeux.JeuxListener;
import modele.serveur.stub.jeux.application.JeuxIF;
import modele.serveur.stub.jeux.application.ResultatPartieEnum;
import javafx.application.Platform;

/*
 * Classe abstraite permettant de faire implementer
 * au controleur de jeux une base de methodes communes
 */
public abstract class CtrlJeu {

    protected CtrlPrincipal parent;

    protected boolean partieLance = false;
    protected boolean peutJouer = false;

    public abstract void initialiser(CtrlPrincipal parent, String pseudo, JeuxListener listener);
    /*
     * Associe l'interface de jeu
     */
    public abstract void setJeuxIF(JeuxIF jeuxIF);
    /*
     * Active / Desactive les composants si c'est au joueur de jouer
     */
    protected abstract void activerComposants(boolean active);

    public void lancerPartie() {
        Platform.runLater(() ->  {
            partieLance = true;
        });
    }

    /*
     * Permet au serveur de faire jouer un joueur (activer ses composants)
     */
    public abstract void faireJouer();
    /*
     * Permet d'affiche une popup de fin de partie qui changera en fonction de la
     * valeur de ResultatPartieEnum
     */
    public abstract void arreterJeu(ResultatPartieEnum resultat, String message);

}
