package controleur.app.amis.template;

import controleur.app.amis.CtrlDemandeAmis;
import modele.serveur.stub.amis.PortailAmisIF;
import modele.serveur.stub.connexion.joueur.JoueurProxy;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.rmi.RemoteException;

public class CtrlTemplateDemande {

    private CtrlDemandeAmis parent;
    private String pseudo;
    private JoueurProxy joueur;
    private PortailAmisIF portailsAmis;

    /*
     * Composants FXML
     */
    @FXML private GridPane root;
    @FXML private Label lbl_nom;
    @FXML private ImageView btn_accepter;
    @FXML private ImageView btn_refuser;

    public void initialiser(CtrlDemandeAmis parent, String pseudo, JoueurProxy joueurProxy, PortailAmisIF portailsAmis) {
        this.parent = parent;
        this.pseudo = pseudo;
        this.joueur = joueurProxy;
        this.portailsAmis = portailsAmis;
        initialiserComposants(joueurProxy);
    }

    private void initialiserComposants(JoueurProxy joueurProxy) {
        this.lbl_nom.setText(joueurProxy.getPseudo());
    }

    @FXML
    private void traiterDemande(MouseEvent event) {
        try {
            portailsAmis.accepterDemande(this.pseudo, joueur.getPseudo(), event.getSource().equals(btn_accepter));
            this.parent.getVbox_demandes().getChildren().remove(this.root);
            this.parent.getLbl_nb_demandes().setText(String.valueOf(this.parent.getVbox_demandes().getChildren().size() - 1));
            if(this.parent.getVbox_demandes().getChildren().size() == 1) this.parent.getLbl_vide().setVisible(true);
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        }
    }

}