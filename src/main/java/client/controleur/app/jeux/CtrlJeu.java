package client.controleur.app.jeux;

import client.controleur.app.CtrlPrincipal;
import client.modele.implementation.jeux.JeuxListener;
import client.modele.serveur.stub.jeux.application.JeuxIF;
import client.modele.serveur.stub.jeux.application.ResultatPartieEnum;
import javafx.application.Platform;
public abstract class CtrlJeu {

    protected CtrlPrincipal parent;

    protected boolean partieLance = false;
    protected boolean peutJouer = false;

    public abstract void initialiser(CtrlPrincipal parent, String pseudo, JeuxListener listener);
    public abstract void setJeuxIF(JeuxIF jeuxIF);
    protected abstract void activerComposants(boolean active);

    public void lancerPartie() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                partieLance = true;
            }
        });
    }
    public abstract void faireJouer();
    public abstract void arreterJeu(ResultatPartieEnum resultat, String message);

}
