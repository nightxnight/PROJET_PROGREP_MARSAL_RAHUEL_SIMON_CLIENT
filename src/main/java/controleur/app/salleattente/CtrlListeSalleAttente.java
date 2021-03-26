package controleur.app.salleattente;

import controleur.app.CtrlPrincipal;
import modele.serveur.stub.connexion.session.SessionIF;
import modele.serveur.stub.jeux.application.JeuxEnum;
import modele.implementation.salleattente.SalleAttenteProxy;
import modele.serveur.stub.salleattente.connecteur.ConnecteurSalleAttenteIF;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CtrlListeSalleAttente implements Initializable {

    private CtrlPrincipal parent;

    private String pseudo;
    private SessionIF session;

    private ConnecteurSalleAttenteIF connecteur;

    /*
     * Composants FXML
     */
    @FXML private ImageView btn_actualiser;
    @FXML private VBox vbox_salleattente;
    @FXML private Button btn_creer_salleattente;
    @FXML private Label lbl_aucun_res;
    @FXML private Button btn_rechercher;
    @FXML private CheckBox cb_pleine;
    @FXML private CheckBox cb_mdp;
    @FXML private ChoiceBox<String> chbox_jeu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<JeuxEnum> listeJeu = JeuxEnum.getListeJeu();
        chbox_jeu.getItems().add("Non selectionne");
        for(JeuxEnum jeu : listeJeu) {
            chbox_jeu.getItems().add(jeu.getNomJeu());
        }
        chbox_jeu.getSelectionModel().select("Non selectionne");
    }

    public void initialiser(CtrlPrincipal parent, String pseudo, SessionIF session) throws Exception {
        this.parent = parent;
        this.pseudo = pseudo;
        this.session = session;
        this.connecteur = session.getConnecteurSalleAttente();
        actualiser(null);
    }

    @FXML
    private void actualiser(MouseEvent event) {
        this.vbox_salleattente.getChildren().clear();
        try {
            ArrayList<SalleAttenteProxy> listeSallesAttentes = connecteur.getListeSallesAttentes();
            for (SalleAttenteProxy salleAttenteProxy : listeSallesAttentes)
                chargerTemplateSalleAttente(salleAttenteProxy);
            if (listeSallesAttentes.size() == 0)
                this.vbox_salleattente.getChildren().add(this.lbl_aucun_res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  void chargerTemplateSalleAttente(SalleAttenteProxy salleAttenteProxy) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/salleattente/template_salleattente.fxml"));
            this.vbox_salleattente.getChildren().add(loader.load());
            CtrlSalleAttenteTemplate controleur = loader.getController();
            controleur.charger(this, connecteur, salleAttenteProxy, pseudo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void creer_salleattente(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/salleattente/creation_salleattente.fxml"));
            parent.getPnl_principal().setCenter(loader.load());
            CtrlCreationSalleAttente controleur = loader.getController();
            controleur.initialiser(parent, pseudo, connecteur);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    @FXML
    void rechercher(MouseEvent event) {
        rechercher(chbox_jeu.getValue(), cb_pleine.isSelected(), cb_mdp.isSelected());
    }

    public void rechercher(String jeu, boolean masquerPleine, boolean masquerMdp) {
        try {
            this.chbox_jeu.getSelectionModel().select(jeu);
            this.cb_pleine.setSelected(masquerPleine);
            this.cb_mdp.setSelected(masquerMdp);

            this.vbox_salleattente.getChildren().clear();
            ArrayList<SalleAttenteProxy> listeSallesAttentes = connecteur.rechercher(jeu, masquerPleine, masquerMdp);
            for (SalleAttenteProxy salleAttenteProxy : listeSallesAttentes)
                chargerTemplateSalleAttente(salleAttenteProxy);
            if (listeSallesAttentes.size() == 0)
                vbox_salleattente.getChildren().add(this.lbl_aucun_res);
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        }
    }


    public CtrlPrincipal getParent() {
        return parent;
    }
}
