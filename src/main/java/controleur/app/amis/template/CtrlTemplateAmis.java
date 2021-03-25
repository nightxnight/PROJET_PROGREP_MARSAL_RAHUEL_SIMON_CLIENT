package controleur.app.amis.template;

import controleur.app.amis.CtrlListeAmis;
import modele.implementation.connexion.joueur.JoueurProxy;
import modele.serveur.stub.amis.PortailAmisIF;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import utils.composants.ConfirmationAlert;
import utils.composants.ErrorAlert;

import java.rmi.RemoteException;

public class CtrlTemplateAmis {

    private CtrlListeAmis parent;
    private String pseudo;
    private PortailAmisIF portailAmis;

    private JoueurProxy joueur;

    private final Image connecte = new Image("/Images/client/app/Amis/connecte.png", 40, 40, true, true);
    private final Image deconnecte = new Image("/Images/client/app/Amis/deconnecte.png", 40, 40, true, true);

    /*
     * Composants FXML
     */
    @FXML private GridPane root;
    @FXML private Label lbl_nom;
    @FXML private ImageView iv_status;
    @FXML private ImageView btn_chat;
    @FXML private ImageView btn_suppr;

    public void initialiser(CtrlListeAmis parent, String pseudo, JoueurProxy joueurProxy, PortailAmisIF portailAmis) {
        this.parent = parent;
        this.pseudo = pseudo;
        this.joueur = joueurProxy;
        this.portailAmis = portailAmis;
        initialiserComposants(joueurProxy);
    }

    private void initialiserComposants(JoueurProxy joueurProxy) {
        this.lbl_nom.setText(joueurProxy.getPseudo());
        actualiserEtat(joueurProxy.isEnLigne());
    }

    public void actualiserEtat(boolean etat) {
        if(etat) iv_status.setImage(connecte);
        else iv_status.setImage(deconnecte);
    }

    @FXML
    void SupprimerAmis(MouseEvent event) {
        try {
            ConfirmationAlert alert = new ConfirmationAlert(joueur.getPseudo() + " va etre supprime de votre liste d'amis.\nEtes-vous sur ?");
            alert.showAndWait();
            boolean valider = alert.getResult().equals(alert.getVALIDER());
            if (valider) {
                portailAmis.supprimerAmis(this.pseudo, joueur.getPseudo());
                parent.getMapPane().get("mesamis").getPremier().getChildren().remove(this.root);
            }
        } catch (IllegalArgumentException iae) {
            new ErrorAlert(iae.getMessage()).showAndWait();
        } catch (RemoteException re) {
            new ErrorAlert("Un probleme de communication est survenue").showAndWait();
        }
    }

    @FXML
    void ouvrirChat(MouseEvent event) {
        parent.getRoot().setRight(parent.getMapChat().get(this.joueur.getPseudo()).getPremier());
    }

}
