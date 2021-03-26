package controleur.app.salleattente;

import modele.serveur.stub.salleattente.SalleAttenteIF;
import modele.serveur.stub.salleattente.SalleAttenteProprietaireIF;
import modele.implementation.salleattente.SalleAttenteProxy;
import modele.serveur.stub.salleattente.connecteur.ConnecteurSalleAttenteIF;
import modele.implementation.salleattente.ListenerSalleAttente;

import modele.client.stub.salleattente.ListenerSalleAttenteIF;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import utils.composants.CustomPasswordDialog;
import utils.composants.ErrorAlert;

import java.rmi.RemoteException;
import java.util.Optional;

/*
 * controleur des templates de salle d'attentes
 * c'est a dire la representation des salles d'attentes
 * dans la liste.
 */
public class CtrlSalleAttenteTemplate {

    private CtrlListeSalleAttente parent;
    private ConnecteurSalleAttenteIF connecteur;
    private String pseudo;

    private String nomSalle;
    private boolean besoinMdp;
    /*
     * Composants FXML
     */
    @FXML private Label lbl_nom_salle;
    @FXML private Label lbl_nom_jeu;
    @FXML private ImageView iv_prive;
    @FXML private Label lbl_nbj_in;
    @FXML private Label lbl_nbj_max;
    @FXML private ImageView btn_rejoindre;

    public void charger(CtrlListeSalleAttente parent, ConnecteurSalleAttenteIF connecteur, SalleAttenteProxy salleAttenteProxy, String pseudo) {
        this.parent = parent;
        this.connecteur = connecteur;
        this.pseudo = pseudo;
        mettreAJourComposants(salleAttenteProxy);
    }

    private void mettreAJourComposants(SalleAttenteProxy salleAttenteProxy) {
        this.nomSalle = salleAttenteProxy.getNomSalle();
        this.lbl_nom_salle.setText(salleAttenteProxy.getNomSalle());

        this.lbl_nom_jeu.setText(salleAttenteProxy.getNomJeu());

        this.lbl_nbj_in.setText(String.valueOf(salleAttenteProxy.getNbJoueursConnecte()));
        this.lbl_nbj_max.setText(String.valueOf(salleAttenteProxy.getNbJoueursMax()));

        this.besoinMdp = salleAttenteProxy.isBesoinMdp();
        this.iv_prive.setVisible(salleAttenteProxy.isBesoinMdp());
    }

    /*
     * Entre dans une salle, les droits proprietaires sont donc null
     */
    @FXML
    private void rejoindreSalle(MouseEvent event) {
        entrer(null);
    }

    /*
     * Demande au connecteur des salles d'attente de rentrer dans une salle.
     * Une popup sera affiche si une erreur est leve
     * Une fenetre de saisie de mot de passe sera affiche si un mot de passe est necessaire
     * pour rejoindre la salle d'attente.
     * Lorsqu'on demande de rejoindre une salle d'attente on passe egalement un listener
     * pour que le serveur puisse nous recontacter
     * Dans le cas ou le joueur entre dans la salle d'attente on recupere une interface
     * salle d'attente puis on initialise le panel salle d'attente
     */
    private void entrer(SalleAttenteProprietaireIF droitProprietaires){
        ListenerSalleAttente listenerSalleAttente = null;
        SalleAttenteIF salleAttenteIF = null;
        try {
            listenerSalleAttente = new ListenerSalleAttente();
            String motDePasse = "";
            if(besoinMdp) {
                CustomPasswordDialog passwordDialog = new CustomPasswordDialog("Vous devez taper le mot de passe de cette salle pour rentrer.");
                Optional<String> resultat = passwordDialog.showAndWait();
                if(resultat.isEmpty()) return;
                else motDePasse = resultat.get();
            }
            salleAttenteIF = connecteur.rejoindre(pseudo, (ListenerSalleAttenteIF) listenerSalleAttente, this.nomSalle, motDePasse);
            if (salleAttenteIF != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/salleattente/salle_attente.fxml"));
                parent.getParent().getMapPane().put("salleattente", loader.load());
                CtrlSalleAttente controleur = loader.getController();
                controleur.initialiser(parent.getParent(), listenerSalleAttente, salleAttenteIF, this.nomSalle, pseudo, droitProprietaires);
                parent.getParent().afficher("salleattente");
            }
        } catch (IllegalArgumentException iae) {
            new ErrorAlert(iae.getMessage()).showAndWait();
        } catch (RemoteException e) {
            new ErrorAlert("Une erreur de communication est survenue.").showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
