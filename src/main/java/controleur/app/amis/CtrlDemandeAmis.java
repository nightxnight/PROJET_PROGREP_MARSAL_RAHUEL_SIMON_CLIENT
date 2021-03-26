package controleur.app.amis;

import controleur.app.amis.template.CtrlTemplateDemande;
import javafx.scene.paint.Paint;
import modele.implementation.connexion.joueur.JoueurProxy;
import modele.serveur.stub.amis.PortailAmisIF;
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
import java.util.HashMap;

/*
 * Gere la liste des demandes d'amis
 */
public class CtrlDemandeAmis extends CtrlAmis {

    private CtrlListeAmis parent;
    private String pseudo;
    private PortailAmisIF portailAmis;

    private HashMap<String, Pane> mapTemplateDemande;

    /*
     * Composants FXML
     */
    @FXML private TextField tf_ajout;
    @FXML private Label lbl_erreur;
    @FXML private ImageView btn_ajout;
    @FXML private VBox vbox_demandes;
    @FXML private Label lbl_vide;

    public void initialiser(CtrlListeAmis parent, String pseudo, PortailAmisIF portailAmisIF) {
        this.parent = parent;
        this.pseudo = pseudo;
        this.portailAmis = portailAmisIF;
        initialiserComposants();
        this.mapTemplateDemande = new HashMap<String, Pane>();
        try { initialiserListe(portailAmis.getListeDemande(this.pseudo)); } catch (RemoteException re) { System.out.println(re.getMessage()); }
    }

    private void initialiserComposants() {
        this.lbl_erreur.setText("");
    }

    /*
     * Permet d'envoyer une demande d'amis au pseudo entrer dans tf_ajout
     * Le label d'erreur se mettera a jour si une erreur d'arguments est
     * lever. Sinon si cela s'est bien passe
     */
    @FXML
    void ajoutAmis(MouseEvent event) {
        if(tf_ajout.getText().trim().equals("")) {
            lbl_erreur.setText("Il faut preciser le nom du joueur.");
        }  else {
            try {
                portailAmis.demandeAmis(this.pseudo, tf_ajout.getText().trim());
                lbl_erreur.setTextFill(Paint.valueOf("#99F1DE"));
                lbl_erreur.setText("Une demande a ete envoye a " + tf_ajout.getText().trim());
            } catch (IllegalArgumentException iae) {
                lbl_erreur.setTextFill(Paint.valueOf("#FF0000"));
                lbl_erreur.setText(iae.getMessage());
            } catch (RemoteException re) {
                System.out.println(re.getMessage());
            }
        }
    }

    @Override
    public void initialiserListe(ArrayList<JoueurProxy> listeDemande) {
        vbox_demandes.getChildren().clear();
        vbox_demandes.getChildren().add(lbl_vide);

        for(JoueurProxy ami : listeDemande)
            ajouterDemande(ami);

        parent.getLbl_nb_demandes().setText(String.valueOf(vbox_demandes.getChildren().size() - 1));
    }

    public void ajouterDemande(JoueurProxy joueurProxy) {
        Platform.runLater(() -> {
            try {
                lbl_vide.setVisible(false);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/amis/template_demande.fxml"));
                Pane pane = loader.load();
                CtrlTemplateDemande controleur = loader.getController();
                controleur.initialiser(this, this.pseudo, joueurProxy, this.portailAmis);
                vbox_demandes.getChildren().add(pane);
                parent.getLbl_nb_demandes().setText(String.valueOf(vbox_demandes.getChildren().size() - 1));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public VBox getVbox_demandes() {
        return vbox_demandes;
    }

    public Label getLbl_nb_demandes() {
        return parent.getLbl_nb_demandes();
    }

    public Label getLbl_vide() {
        return lbl_vide;
    }
}
