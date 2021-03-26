package controleur.app.salleattente;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import modele.implementation.connexion.joueur.JoueurProxy;

/*
 * controleur des templates des joueurs dans
 * la salle d'attente.
 * Ils permettent a un joueur de connaitre les autres
 * joueurs present dans la salle d'attente.
 */
public class CtrlSalleAttenteJoueurTempl {

    CtrlSalleAttente parent;

    private String pseudoJoueur;

    /*
     * Composants FXML
     */
    @FXML private Label lbl_nom_joueur;
    @FXML private Circle circle_pret;
    @FXML private ImageView iv_proprietaire;
    @FXML private ImageView btn_designer_proprietaire;
    @FXML private ImageView btn_exclure;

    public void initialiser(CtrlSalleAttente parent, JoueurProxy joueur, boolean proprietaire, boolean active, boolean pret) {
        this.parent = parent;
        this.pseudoJoueur = joueur.getPseudo();
        this.lbl_nom_joueur.setText(joueur.getPseudo());
        this.iv_proprietaire.setVisible(proprietaire);
        this.btn_designer_proprietaire.setDisable(!active); btn_designer_proprietaire.setVisible(active);
        this.btn_exclure.setDisable(!active); btn_exclure.setVisible(active);
        changerEtat(pret);
    }

    /*
     * Permet de changer la couleur du cercle representant le status d'un joueur (pret / pas pret)
     */
    public void changerEtat(boolean pret) {
        if(pret) circle_pret.fillProperty().setValue(Paint.valueOf("#00ff00"));
        else circle_pret.fillProperty().setValue(Paint.valueOf("#ff0000"));
    }


    @FXML
    void exclure(MouseEvent event) {
        parent.exclure(this.pseudoJoueur);
    }

    @FXML
    void nommerProprietaire(MouseEvent event) {
        parent.nommerProprietaire(this.pseudoJoueur);
    }


}
