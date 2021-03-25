package client.controleur.app.amis.chat;

import client.controleur.app.amis.CtrlListeAmis;
import client.modele.serveur.stub.amis.PortailAmisIF;
import client.modele.serveur.stub.amis.chat.InvitationMessage;
import client.modele.serveur.stub.amis.chat.Message;
import client.modele.serveur.stub.amis.chat.SimpleMessage;
import client.modele.serveur.stub.connexion.joueur.JoueurProxy;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class CtrlChatAmis {

    private CtrlListeAmis parent;
    private String pseudo;
    private JoueurProxy joueur;
    private PortailAmisIF portailAmis;

    /*
     * Composants FXML
     */
    @FXML private ImageView btn_fermer;
    @FXML private Label lbl_nomjoueur;
    @FXML private VBox vBox_message;
    @FXML private TextField tf_message;
    @FXML private ImageView btn_envoyer;

    public void initialiser(CtrlListeAmis parent, String pseudo, JoueurProxy joueurProxy, PortailAmisIF portailAmis) {
        this.parent = parent;
        this.pseudo = pseudo;
        this.joueur = joueurProxy;
        this.portailAmis = portailAmis;
        initialiserComposants();
        initialiserChat();
    }

    private void initialiserComposants() {
        this.lbl_nomjoueur.setText(joueur.getPseudo());
    }

    private void initialiserChat() {
        try {
            ArrayList<Message> listeMessage = portailAmis.getConversation(this.pseudo, joueur.getPseudo());
            if(listeMessage.size() == 0) {
                vBox_message.getChildren().add(new Label("C'est le debut de votre conversation avec " + joueur.getPseudo() + "\nEnvoyez lui un message!"));
            } else
                for(Message message : listeMessage) {
                    if (message instanceof SimpleMessage) afficherSimpleMessage((SimpleMessage) message);
                }
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        }
    }

    private void afficherSimpleMessage(SimpleMessage message) {
        vBox_message.getChildren().add(new Label(message.getDe() + ": " + message.getContenu()));
    }

    private void afficherInvitation(InvitationMessage invitation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/amis/invitation.fxml"));
            Pane pane = loader.load();
            CtrlInvitation controleur = loader.getController();
            controleur.initialiser(parent.getParent(), this, parent.getParent().getSession(), parent.getParent().getPseudo(), invitation);
            vBox_message.getChildren().add(pane);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void envoyerMessage(MouseEvent event) {
        try {
            if(tf_message.getText().trim().equals("")) return;
            portailAmis.envoyerMessage(this.pseudo, joueur.getPseudo(), new SimpleMessage(this.pseudo, joueur.getPseudo(), tf_message.getText().trim()));
            tf_message.setText("");
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        }
    }

    @FXML
    private void fermerChat(MouseEvent event) {
        parent.getRoot().setRight(null);
    }

    /*
     * Methodes appelees par le serveur
     */
    public void recupererMessage(Message message) {
        Platform.runLater(() -> {
            if(message instanceof SimpleMessage)
                afficherSimpleMessage((SimpleMessage) message);
            else if(message instanceof InvitationMessage)
                afficherInvitation((InvitationMessage) message);
        });
    }

    public VBox getvBox_message() {
        return vBox_message;
    }
}
