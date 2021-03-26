package controleur.app.jeux.listejeu;

import controleur.app.CtrlPrincipal;
import controleur.app.salleattente.CtrlListeSalleAttente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import modele.serveur.stub.jeux.application.JeuxEnum;

import java.util.ArrayList;

/*
 * Permet d'afficher la liste des jeux disponible sur le serveur
 * Si l'on clique sur un jeu alors on sera ramener au panel des salles
 * d'attente et on recherchera uniquement les salles d'attentes qui jouent a
 * ce jeu.
 */

public class CtrlListeJeu {

    private CtrlPrincipal parent;
    private CtrlListeSalleAttente ctrlListeSalleAttente;


    /*
     * Composants FXML
     */
    @FXML private FlowPane root;

    public void initialiser(CtrlPrincipal parent, CtrlListeSalleAttente ctrlListeSalleAttente) {
        this.parent = parent;
        this.ctrlListeSalleAttente = ctrlListeSalleAttente;
        initialiserComposants();
    }

    /*
     * Cree les panels de jeu en fonction de l'enumeration JeuxEnum
     */
    private void initialiserComposants() {
        ArrayList<JeuxEnum> listeJeux = JeuxEnum.getListeJeu();
        for (JeuxEnum jeu : listeJeux) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/jeux/template_jeu.fxml"));
                Pane pane = loader.load();
                CtrlTemplateJeu controleur = loader.getController();
                controleur.initialiser(parent, ctrlListeSalleAttente, jeu.getNomJeu(), jeu.getDescription(),
                        new Image("./Images/client/app/jeux/icons/" + jeu.getNomJeu() + ".png"));
                root.getChildren().add(pane);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

