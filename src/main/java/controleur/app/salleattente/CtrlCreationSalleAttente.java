package controleur.app.salleattente;

import controleur.app.CtrlPrincipal;
import modele.implementation.salleattente.ListenerSalleAttente;
import modele.serveur.stub.salleattente.SalleAttenteIF;
import modele.serveur.stub.salleattente.SalleAttenteProprietaireIF;
import modele.serveur.stub.salleattente.connecteur.ConnecteurSalleAttenteIF;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class CtrlCreationSalleAttente {

    private CtrlPrincipal parent;
    private String pseudo;
    private ConnecteurSalleAttenteIF connecteur;
    /*
     * Composants FXML
     */
    @FXML private TextField tf_nom_salle;
    @FXML private PasswordField pf_mdp;
    @FXML private CheckBox cb_publique;
    @FXML private Label lbl_erreur;
    @FXML private Button btn_creer;

    public void initialiser(CtrlPrincipal parent, String pseudo, ConnecteurSalleAttenteIF connecteur) {
        this.parent = parent;
        this.pseudo = pseudo;
        this.connecteur = connecteur;
        lbl_erreur.setText("");
    }


    @FXML
    void creer_salleattente(MouseEvent event) {
        String nomSalle = tf_nom_salle.getText().trim();
        if(nomSalle.equals("")) {
            lbl_erreur.setText("Il faut preciser le nom de votre salle");
        }

        String mdp = pf_mdp.getText().trim();

        boolean publique  = !cb_publique.isSelected();

        SalleAttenteProprietaireIF droitsProprietaire = null;
        SalleAttenteIF salleAttenteIF;
        ListenerSalleAttente listenerSalleAttente = null;
        try {
            listenerSalleAttente = new ListenerSalleAttente();
            droitsProprietaire = connecteur.creer(pseudo, nomSalle, mdp, publique);
            salleAttenteIF = connecteur.rejoindre(pseudo, listenerSalleAttente, nomSalle, mdp);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/salleattente/salle_attente.fxml"));
            parent.getMapPane().put("salleattente", loader.load());
            CtrlSalleAttente controleur = loader.getController();
            controleur.initialiser(parent, listenerSalleAttente, salleAttenteIF, nomSalle, pseudo, droitsProprietaire);
            parent.afficher("salleattente");
        } catch (IllegalArgumentException iae) {
            lbl_erreur.setText(iae.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void retour_liste(MouseEvent event) {
        parent.afficher("liste_salleattente");
    }
}