package controleur.app.options;

import controleur.app.CtrlPrincipal;
import javafx.stage.Stage;
import modele.implementation.connexion.joueur.Joueur;
import modele.serveur.stub.connexion.session.SessionIF;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import utils.composants.ConfirmationAlert;
import utils.composants.InformationAlert;
import vue.app.Connexion;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class CtrlOptions {

    private CtrlPrincipal parent;

    private String pseudo;
    private SessionIF session;

    private final static DateTimeFormatter formateur = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /*
     * Composants FXML
     */
    @FXML private RadioButton rb_theme_sombre;
    @FXML private RadioButton rb_theme_clair;
    @FXML private ToggleGroup tg_theme;
    @FXML private TextField tf_pseudo;
    @FXML private TextField tf_mail;
    @FXML private Label lbl_erreur_mail;
    @FXML private PasswordField pf_ancienmdp;
    @FXML private Label lbl_erreur_ancienmdp;
    @FXML private PasswordField pf_mdp;
    @FXML private Label lbl_erreur_mdp;
    @FXML private PasswordField pf_confirm;
    @FXML private Label lbl_erreur_confirm;
    @FXML private DatePicker dp_anniv;
    @FXML private Label lbl_erreur_anniv;
    @FXML private TextField tf_date_inscrit;
    @FXML private Label lbl_erreur_modif;
    @FXML private Button btn_valide;
    @FXML private Button btn_deconnexion;

    public void initialiser(CtrlPrincipal parent, String pseudo, SessionIF session) {
        this.parent = parent;
        this.pseudo = pseudo;
        this.session = session;
        try {
            initialiserComposants(session.getProfil());
        } catch (RemoteException re) {
            re.printStackTrace();
            System.exit(1);
        }
    }

    private void initialiserComposants(Joueur joueur) {
        tf_pseudo.setText(joueur.getPseudo());
        tf_mail.setText(joueur.getMail());
        dp_anniv.setValue(LocalDate.from(formateur.parse(joueur.getAnniversaire())));
        tf_date_inscrit.setText(joueur.getInscription());
        lbl_erreur_mail.setText("");
        lbl_erreur_ancienmdp.setText("");
        lbl_erreur_mdp.setText("");
        lbl_erreur_confirm.setText("");
        lbl_erreur_anniv.setText("");
        this.lbl_erreur_modif.setText("");
    }


    @FXML
    void appliquerChangements(MouseEvent event) {
        if (verifSaisie()) {
            ConfirmationAlert confirmation = new ConfirmationAlert("Votre profil va etre modifie.\nEtes vous sur ?");
            confirmation.showAndWait();
            if (confirmation.getResult().equals(confirmation.getANNULER())) return;
            String mail = tf_mail.getText().trim();
            String ancienMotDePasse = pf_ancienmdp.getText().trim();
            String nouveauMotDePasse = pf_mdp.getText().trim();
            LocalDate dateAnniv = dp_anniv.getValue();
            boolean modif = false;

            try {
                modif = session.modifierProfil(ancienMotDePasse, nouveauMotDePasse, mail, dateAnniv);
            } catch (IllegalArgumentException iae) {
                lbl_erreur_modif.setText(iae.getMessage());
                lbl_erreur_modif.setTextFill(Paint.valueOf("#ff0000"));
            } catch (RemoteException re) {
                re.printStackTrace();
            }

            if (modif) {
                lbl_erreur_modif.setText("Les modifications ont bien ete prises en compte.");
                lbl_erreur_modif.setTextFill(Paint.valueOf("#00ff93"));
            }

        }
    }

    private boolean verifSaisie(){
        boolean saisieValide = true;

        if (tg_theme.getSelectedToggle().equals(rb_theme_clair)) {
            unThemeClairSerieux(); /* C'est une blague evidemment */
            saisieValide = false;
        }

        if (tf_mail.getText().trim().equals("")) {
            lbl_erreur_mail.setText("L'adresse mail doit etre preciser.");
            saisieValide = false;
        } else if (!Pattern.matches("^(.+)@(.+)$", tf_mail.getText().trim())){
            lbl_erreur_mail.setText("Ce n'est pas une adresse mail valide.");
            saisieValide = false;
        } else lbl_erreur_mail.setText("");

        if(pf_ancienmdp.getText().equals("")) {
            lbl_erreur_ancienmdp.setText("Il faut saisir votre mot de passe actuel.");
            saisieValide = false;
        } else lbl_erreur_ancienmdp.setText("");

        if (pf_mdp.getText().trim().equals("")){
            lbl_erreur_mdp.setText("Il faut saisir un mot de passe.");
            saisieValide = false;
        } else lbl_erreur_mdp.setText("");

        if(pf_confirm.getText().trim().equals("")) {
            lbl_erreur_confirm.setText("Il faut confirmer votre mot de passe");
            saisieValide = false;
        } else if (!pf_confirm.getText().trim().equals(pf_mdp.getText().trim())) {
            lbl_erreur_confirm.setText("Le mot de passe ne correspond pas.");
            saisieValide = false;
        } else lbl_erreur_confirm.setText("");

        if(dp_anniv.getValue().isAfter(LocalDate.now())) {
            lbl_erreur_anniv.setText("La date d'anniversaire n'est pas valide.");
            saisieValide = false;
        } else lbl_erreur_anniv.setText("");

        return saisieValide;
    }

    private void unThemeClairSerieux(){
        InformationAlert alert = new InformationAlert(
                "Il semble que vous n'allez pas bien. Veuillez contacter le SAMU au plus vite (Tel : 15)\n" +
                "Qui met un theme clair en 2021 serieux ?");
        alert.setHeaderText("Alerte information");
        alert.showAndWait();
    }

    @FXML
    void seDeconnecter(MouseEvent event) {
        ConfirmationAlert confirmation = new ConfirmationAlert("Vous allez etre deconnecte.\nEtes vous sur ?");
        confirmation.showAndWait();
        if (confirmation.getResult().equals(confirmation.getANNULER())) return;
        try {
            session.logout();
            ((Stage) btn_deconnexion.getScene().getWindow()).close();
            new Connexion().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
