package controleur.app.connexion;

import modele.serveur.stub.connexion.connecteur.ConnecteurSessionIF;
import modele.serveur.stub.connexion.session.SessionIF;
import vue.app.App;
import vue.app.Connexion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

public class CtrlConnexion implements Initializable {

    private ConnecteurSessionIF connecteur;

    /*
     * Composants FXML
     */
    @FXML private BorderPane root;
    @FXML private Text lbl_nom;
    @FXML private Text lbl_mdp;
    @FXML private Hyperlink lk_inscription;
    @FXML private TextField tf_utilisateur;
    @FXML private PasswordField pf_mdp;
    @FXML private Label lbl_erreur;
    @FXML private Button btn_connexion;

    @FXML private GridPane pnl_connexion;
    private GridPane pnl_inscription;

    @FXML
    void Tf_Rempli(KeyEvent event) {

    }

    /*
     * Permet de recuperer l'interface qui permet de se connecter et par consequent
     * obtenir une session / acceder au reste de l'application
     * Initialise egalement panel d'inscription
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/connexion/inscription.fxml"));
            this.pnl_inscription = loader.load();
            CtrlInscription controleur = loader.getController();
            final Registry registry = LocateRegistry.getRegistry(Connexion.HOST, Connexion.PORT);
            connecteur = (ConnecteurSessionIF) registry.lookup("connexion");
            controleur.initialiser(this, this.pnl_connexion, connecteur);
            lbl_erreur.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /*
     * Permet de basculer sur le panel d'inscription
     */
    @FXML
    void allerInscription(MouseEvent event) {
        this.root.setCenter(pnl_inscription);
    }

    /*
     * Permet d'appeler une methode sur le connecteur afin
     * d'obtenir une session. Le label d'erreur se met a jour
     * si une erreur est leve.
     */
    @FXML
    void seConnecter(MouseEvent event) {
        String pseudo = tf_utilisateur.getText().trim();
        if (pseudo.equals("")) {
            lbl_erreur.setText("Il faut preciser votre pseudo.");
            return;
        }

        String motDePasse = pf_mdp.getText().trim();
        if (motDePasse.equals("")) {
            lbl_erreur.setText("Il faut preciser votre mot de passe.");
            return;
        }

        try {
            ouvrirApplication((Stage) pnl_connexion.getScene().getWindow(), pseudo, connecteur.seConnecter(pseudo, motDePasse));
        } catch (IllegalArgumentException iae) {
            lbl_erreur.setText(iae.getMessage());
        } catch (RemoteException re) {
            lbl_erreur.setText("Un probleme de communication est survenue.");
            re.printStackTrace();
        }
    }

    /*
     * Permet de lancer l'application principal
     */
    public void ouvrirApplication(Stage ancienneFenetre, String pseudo, SessionIF session) {
        try {
            new App(session, pseudo);
            ancienneFenetre.close();
        } catch (RemoteException re) {
            this.root.setCenter(pnl_connexion);
            this.lbl_erreur.setText("Une erreur de communication est survenue.");
        } catch (Exception e) {
            System.exit(1);
        }
     }

    public BorderPane getRoot() {
        return root;
    }

}
