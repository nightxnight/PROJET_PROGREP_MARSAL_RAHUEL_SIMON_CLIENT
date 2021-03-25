package client.controleur.app;

import client.controleur.app.amis.CtrlListeAmis;
import client.controleur.app.options.CtrlOptions;
import client.controleur.app.salleattente.CtrlListeSalleAttente;
import client.modele.serveur.stub.connexion.joueur.Joueur;
import client.modele.serveur.stub.connexion.session.SessionIF;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class CtrlPrincipal {

    private String pseudo;
    private SessionIF session;
    private Joueur joueur;

    /*
     * Composants du fxml
     */
    @FXML private BorderPane pnl_principal;
    @FXML private Label lbl_fil_ariane;

    private BorderPane pnl_menu;
    private HashMap<String, Pane> mapPane;

    public void initialiser(String pseudo, SessionIF session) {
        this.pseudo = pseudo;
        this.session = session;
        try {
            chargerMenuBar();
            chargerOnglets();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chargerMenuBar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/menubar.fxml"));
            this.pnl_menu = loader.load();
            CtrlMenuBar controleurMenuBar = loader.getController();
            controleurMenuBar.setParent(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.pnl_principal.setLeft(pnl_menu);
    }

    private void chargerOnglets() {
        this.mapPane = new HashMap<String, Pane>();
        try {
            FXMLLoader loader = null;
            /*
             * Profil
             */
            loader = prepareFXMLLoader("accueil/accueil.fxml");
            mapPane.put("accueil", loader.load());
            /*
             * Amis
             */
            loader = prepareFXMLLoader("amis/liste_amis.fxml");
            mapPane.put("liste_amis", loader.load());
            CtrlListeAmis ctrlListeAmis = loader.getController();
            ctrlListeAmis.initialiser(this, pseudo, session);
            /*
             * Salle d'attente
             */
            loader = prepareFXMLLoader("salleattente/liste_salleattente.fxml");
            mapPane.put("liste_salleattente", loader.load());
            CtrlListeSalleAttente ctrlListeSalleAttente = loader.getController();
            ctrlListeSalleAttente.initialiser(this, pseudo, session);

            loader = prepareFXMLLoader("options/options.fxml");
            mapPane.put("options", loader.load());
            CtrlOptions ctrlOptions = loader.getController();
            ctrlOptions.initialiser(this, pseudo, session);

            afficher("accueil");

        }  catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private FXMLLoader prepareFXMLLoader(String chemin) {
        return new FXMLLoader(getClass().getResource("/fxml/client/app/" + chemin));
    }

    public void afficher(String nomPane) {
        this.pnl_principal.setCenter(mapPane.get(nomPane));
    }

    public void ouvrirAPropos(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("A propos");
        alert.setHeaderText("A propos");
        String s ="Ce projet à été réalisé par Rémi Marsal, Victor Rahuel et Marco Simon";
        alert.setContentText(s);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("css/Main.css");

        alert.show();
    }

    public HashMap<String, Pane> getMapPane() {
        return mapPane;
    }

    public BorderPane getPnl_principal() {
        return pnl_principal;
    }

    public String getPseudo() {
        return pseudo;
    }

    public SessionIF getSession() {
        return session;
    }
}
