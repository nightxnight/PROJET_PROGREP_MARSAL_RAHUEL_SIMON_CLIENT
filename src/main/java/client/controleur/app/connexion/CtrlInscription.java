package client.controleur.app.connexion;


import client.modele.serveur.stub.connexion.connecteur.ConnecteurSessionIF;
import client.modele.serveur.stub.connexion.session.SessionIF;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class CtrlInscription {

    private CtrlConnexion parent;
    private ConnecteurSessionIF connecteur;
    /*
     * Composants FXML
     */
    @FXML private TextField tf_pseudo;
    @FXML private Label lbl_erreur_pseudo;
    @FXML private TextField tf_mail;
    @FXML private Label lbl_erreur_mail;
    @FXML private DatePicker dp_anniv;
    @FXML private Label lbl_erreur_anniv;
    @FXML private PasswordField pf_mdp;
    @FXML private Label lbl_erreur_mdp;
    @FXML private PasswordField pf_confirmation;
    @FXML private Label lbl_erreur_confirm;
    @FXML private Button btn_inscription;
    @FXML private Label lbl_erreur;
    @FXML private Hyperlink lk_compte;

    private GridPane pnl_connexion;

    public void initialiser(CtrlConnexion parent, GridPane pnl_connexion, ConnecteurSessionIF connecteur) {
        this.parent = parent;
        this.pnl_connexion = pnl_connexion;
        this.connecteur = connecteur;
        initialiserComposants();
    }

    private void initialiserComposants() {
        lbl_erreur_pseudo.setText("");
        lbl_erreur_mail.setText("");
        lbl_erreur_anniv.setText("");
        lbl_erreur_mdp.setText("");
        lbl_erreur_confirm.setText("");
        lbl_erreur.setText("");
    }

    @FXML
    void retourConnexion(MouseEvent event) {
        this.parent.getRoot().setCenter(pnl_connexion);
    }

    @FXML
    void seInscrire(MouseEvent event) {
        boolean saisieValide = verifSaisie();

        if(saisieValide) {
            String pseudo = tf_pseudo.getText().trim();
            String mail = tf_mail.getText().trim();
            LocalDate anniv = dp_anniv.getValue();
            String motDePasse = pf_confirmation.getText().trim();

            SessionIF session = null;
            try {
                session = connecteur.seInscrire(pseudo, motDePasse, mail, anniv);
            } catch (IllegalArgumentException iae) {
                lbl_erreur.setText(iae.getMessage());
            } catch (RemoteException re) {
                lbl_erreur.setText("Un probleme de communication est survenue.");
            }

            if (session != null) {
                parent.ouvrirApplication((Stage) btn_inscription.getScene().getWindow(), pseudo, session);
            }
        }
    }

    private boolean verifSaisie() {
        boolean saisieValide = true;

        if (tf_pseudo.getText().trim().equals("")) {
            lbl_erreur_pseudo.setText("a preciser.");
            saisieValide = false;
        } lbl_erreur_pseudo.setText("");

        if (tf_mail.getText().trim().equals("")) {
            lbl_erreur_mail.setText("a preciser.");
            saisieValide = false;
        } else if (!Pattern.matches("^(.+)@(.+)$", tf_mail.getText().trim())) {
            lbl_erreur_mail.setText("adresse mail invalide.");
            saisieValide = false;
        } else lbl_erreur_mail.setText("");

        if (dp_anniv.getValue() == null) {
            lbl_erreur_anniv.setText("a preciser.");
            saisieValide = false;
        } else if (dp_anniv.getValue().isAfter(LocalDate.now())) {
            lbl_erreur_anniv.setText("date invalide.");
            saisieValide = false;
        } else lbl_erreur_anniv.setText("");

        if (pf_mdp.getText().trim().equals("")) {
            lbl_erreur_mdp.setText("a preciser.");
            saisieValide = false;
        } else lbl_erreur_mdp.setText("");

        if (pf_confirmation.getText().trim().equals("")) {
            lbl_erreur_confirm.setText("a preciser.");
            saisieValide = false;
        } else if (!pf_confirmation.getText().trim().equals(pf_mdp.getText().trim())) {
            lbl_erreur_confirm.setText("ne correspond pas.");
            saisieValide = false;
        } else lbl_erreur_confirm.setText("");

        return saisieValide;
    }

}
