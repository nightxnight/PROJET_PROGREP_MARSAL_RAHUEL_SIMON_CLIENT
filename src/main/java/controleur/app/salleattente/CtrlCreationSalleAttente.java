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

/*
 * controleur de la creation de salle d'attente
 */
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

    /*
     * Cree une salle d'attente en fonction des valeurs
     * de tf_nom_salle, pf_mdp et cb_publique
     * Le label d'erreur est mis a jour si la saisie est invalide
     * / une erreur est leve
     *
     * On obtiendra donc une interface proprietaire (pour executer les fonctions
     * proprietaire) et une interface de salle d'attente classique (celle commune
     * aux joueurs normaux dans la salle d'attente)
     *
     * Le panel de salle d'attente sera donc charger et initialiser
     */
    @FXML
    void creer_salleattente(MouseEvent event) {
        String nomSalle = tf_nom_salle.getText().trim();
        if(nomSalle.equals("")) {
            lbl_erreur.setText("Il faut preciser le nom de votre salle");
            return;
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

    /*
     * Retour au liste des salles d'attentes
     */
    @FXML
    void retour_liste(MouseEvent event) {
        parent.afficher("liste_salleattente");
    }
}