package controleur.app.jeux.listejeu;

import controleur.app.CtrlPrincipal;
import controleur.app.salleattente.CtrlListeSalleAttente;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class CtrlTemplateJeu {

    private CtrlPrincipal parent;
    private CtrlListeSalleAttente ctrlListeSalleAttente;

    /*
     * Composants FXML
     */
    @FXML private ImageView iv_logo;
    @FXML private FlowPane pnl_infos;
    @FXML private Label lbl_nom_jeu;
    @FXML private Label lbl_description;

    public void initialiser(CtrlPrincipal parent, CtrlListeSalleAttente ctrlListeSalleAttente, String nomJeu, String descriptionJeu, Image imageJeu) {
        this.parent = parent;
        this.ctrlListeSalleAttente = ctrlListeSalleAttente;
        lbl_nom_jeu.setText(nomJeu);
        lbl_description.setText(descriptionJeu);
        iv_logo.setImage(imageJeu);
    }

    /*
     * permet de rechercher les salles d'attentes qui jouent au jeu correspondant
     */
    @FXML
    void chercherJeu(MouseEvent event) {
        ctrlListeSalleAttente.rechercher(lbl_nom_jeu.getText(), false, false);
        if(parent.getMapPane().containsKey("salleattente"))
            parent.afficher("salleattente");
        else parent.afficher("liste_salleattente");
    }

    /*
     * Affiche les infos sur le jeux si la souris est au dessus du panel
     */
    @FXML
    void afficherinfos(MouseEvent event) {
        pnl_infos.setVisible(true);
    }


    @FXML
    void masquerinfos(MouseEvent event) {
        pnl_infos.setVisible(false);
    }

}
