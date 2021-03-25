package controleur.app.salleattente.inviter;

import modele.implementation.connexion.joueur.JoueurProxy;
import modele.serveur.stub.amis.PortailAmisIF;
import modele.serveur.stub.connexion.session.SessionIF;
import modele.serveur.stub.salleattente.SalleAttenteIF;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CtrlInviterAmis {

    private SessionIF session;
    private String pseudo;
    private SalleAttenteIF salleAttenteIF;

    /*
     * Composants FXML
     */
    @FXML private VBox vBox_amis;
    @FXML private Button btn_annuler;
    @FXML private Label lbl_vide;

    public void initialiser(SessionIF session, String pseudo, SalleAttenteIF salleAttente) {
        this.session = session;
        this.pseudo = pseudo;
        this.salleAttenteIF = salleAttente;
        initialiserComposants();
    }

    private void initialiserComposants() {
        try {
            PortailAmisIF portailAmis = session.getPortailsAmis(null);
            ArrayList<JoueurProxy> listeAmis = portailAmis.getListeAmis(this.pseudo);
            listeAmis = listeAmis.stream().filter(JoueurProxy::isEnLigne).collect(Collectors.toCollection(ArrayList::new));
            for(JoueurProxy ami : listeAmis)
                creerPanelInvitationAmi(ami);
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        }
    }

    private void creerPanelInvitationAmi(JoueurProxy joueurProxy) {
        lbl_vide.setVisible(false);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/salleattente/template_invitation_ami.fxml"));
            Pane pane = loader.load();
            CtrlTemplateInvitationAmi controleur = loader.getController();
            controleur.initialiser(this, joueurProxy, this.pseudo, salleAttenteIF);
            vBox_amis.getChildren().add(pane);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void annuler(MouseEvent event) {
        ((Stage) this.vBox_amis.getScene().getWindow()).close();
    }

    public VBox getvBox_amis() {
        return vBox_amis;
    }

    public Label getLbl_vide() {
        return lbl_vide;
    }
}
