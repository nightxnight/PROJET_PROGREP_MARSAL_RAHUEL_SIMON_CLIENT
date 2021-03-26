package controleur.app;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import utils.composants.InformationAlert;

/*
 * Controleur de la barre de menu
 */
public class CtrlMenuBar {

    CtrlPrincipal parent;

    /*
     * Composants FXML
     */
    @FXML private ButtonBar btn_salleattente;
    @FXML private ButtonBar btn_jeux;
    @FXML private ButtonBar btn_amis;
    @FXML private ButtonBar btn_apropos;
    @FXML private ButtonBar btn_parametres;
    @FXML private ButtonBar btn_accueil;

    /*
     * Affiche l'onglet correspondant au bouton clique
     */
    @FXML
    public void changerOnglet(MouseEvent event) {
        ButtonBar source = (ButtonBar) event.getSource();

        if (source.equals(btn_accueil)) parent.afficher("accueil");
        else if (source.equals(btn_amis)) parent.afficher("liste_amis");
        else if (source.equals(btn_jeux)) {
            if(parent.getMapPane().containsKey("jeu")) parent.afficher("jeu");
            else parent.afficher("liste_jeu");
        } else if (source.equals(btn_salleattente)) {
            if (parent.getMapPane().containsKey("salleattente")) parent.afficher("salleattente");
            else parent.afficher("liste_salleattente");
        } else if (source.equals(btn_parametres)) parent.afficher("options");
        else if (source.equals(btn_apropos)) {
            InformationAlert apropos = new InformationAlert("Ce projet a ete developpe dans un cadre scolaire.\n" +
                    "Equipe de developpement : MARSAL Remi, RAHUEL Victor,\nSIMON Marco - Mars 2021.");
            apropos.showAndWait();
        }
    }


    public void setParent(CtrlPrincipal parent) {
        this.parent = parent;
    }
}
