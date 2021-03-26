package controleur.app.amis.chat;

import controleur.app.CtrlPrincipal;
import controleur.app.salleattente.CtrlSalleAttente;
import modele.implementation.amis.chat.InvitationMessage;
import modele.implementation.salleattente.ListenerSalleAttente;
import modele.serveur.stub.connexion.session.SessionIF;
import modele.serveur.stub.salleattente.SalleAttenteIF;
import modele.serveur.stub.salleattente.connecteur.ConnecteurSalleAttenteIF;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/*
 * Les invitations permettent d'inviter un amis dans une  salle  d'attente.
 * Cela permet de rendre une salle prive, c'est a dire qu'elle n'apparaitra
 * pas dans la liste des salles d'attentes.
 */
public class CtrlInvitation {

    private CtrlPrincipal ctrlPrincipal;
    private CtrlChatAmis parent;

    private SessionIF session;
    private String pseudo;
    private InvitationMessage invitation;

    /*
     * Composants FXML
     */
    @FXML private GridPane root;
    @FXML private Label lbl_pseudo;
    @FXML private Label lbl_message;
    @FXML private ImageView btn_rejoindre;

    public void initialiser(CtrlPrincipal ctrlPrincipal, CtrlChatAmis parent, SessionIF session, String pseudo, InvitationMessage invitationMessage) {
        this.ctrlPrincipal = ctrlPrincipal;
        this.parent = parent;
        this.session = session;
        this.pseudo = pseudo;
        this.invitation = invitationMessage;
        initialiserComposants();
    }

    private void initialiserComposants() {
        this.lbl_pseudo.setText(invitation.getDe());
        this.lbl_message.setText(invitation.getContenu());
    }

    /*
     * Charge le FXML de la salle d'attente
     * Cree un listener de salle d'attente
     * Initialise Le controleur de salle d'attente
     * Supprime l'invitation
     * Affiche le panel de salle d'attente
     */
    @FXML
    void rejoindresalle(MouseEvent event) {
        try {
            ConnecteurSalleAttenteIF connecteur = session.getConnecteurSalleAttente();
            ListenerSalleAttente listener = new ListenerSalleAttente();
            SalleAttenteIF salleAttente = connecteur.rejoindre(this.pseudo, listener, invitation.getNomSalle(), invitation.getMotDePasseSalle());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/salleattente/salle_attente.fxml"));
            ctrlPrincipal.getMapPane().put("salleattente", loader.load());
            CtrlSalleAttente controleur = loader.getController();
            controleur.initialiser(ctrlPrincipal, listener, salleAttente, this.invitation.getNomSalle() , this.pseudo, null);
            ctrlPrincipal.afficher("salleattente");
            parent.getvBox_message().getChildren().remove(root);
        } catch (IllegalArgumentException iae) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, iae.getMessage(), ButtonType.OK);
            alert.showAndWait();
            parent.getvBox_message().getChildren().remove(root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
