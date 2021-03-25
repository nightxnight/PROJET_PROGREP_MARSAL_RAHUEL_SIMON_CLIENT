package controleur.app.salleattente.inviter;

import modele.implementation.connexion.joueur.JoueurProxy;
import modele.serveur.stub.salleattente.SalleAttenteIF;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.rmi.RemoteException;

public class CtrlTemplateInvitationAmi {

    private CtrlInviterAmis parent;
    private JoueurProxy joueur;
    private String pseudo;
    private SalleAttenteIF salleAttente;

    /*
     * Composants FXML
     */
    @FXML private GridPane root;
    @FXML private Label lbl_pseudo;
    @FXML private ImageView btn_inviter;

    public void initialiser(CtrlInviterAmis parent, JoueurProxy joueurProxy, String pseudo, SalleAttenteIF salleAttente) {
        this.parent = parent;
        this.joueur = joueurProxy;
        this.pseudo = pseudo;
        this.salleAttente = salleAttente;
        initialiserComposants();
    }

    private void initialiserComposants() {
        this.lbl_pseudo.setText(joueur.getPseudo());
    }

    @FXML
    void inviterAmi(MouseEvent event) {
        try {
            salleAttente.inviterAmi(this.pseudo, joueur.getPseudo());
            parent.getvBox_amis().getChildren().remove(this.root);
            if(parent.getvBox_amis().getChildren().size() == 1) parent.getLbl_vide().setVisible(true);
            Stage stage = (Stage) parent.getvBox_amis().getScene().getWindow();
            stage.close();
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        } catch (IllegalArgumentException iae) {
            Alert alert = new Alert(Alert.AlertType.ERROR, iae.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

}
