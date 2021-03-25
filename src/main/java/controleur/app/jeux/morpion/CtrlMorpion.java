package controleur.app.jeux.morpion;

import controleur.app.CtrlPrincipal;
import controleur.app.jeux.CtrlJeu;
import modele.implementation.jeux.JeuxListener;
import modele.implementation.jeux.morpion.MorpionListener;
import modele.serveur.stub.jeux.application.JeuxIF;
import modele.serveur.stub.jeux.application.ResultatPartieEnum;
import modele.serveur.stub.jeux.application.morpion.MorpionIF;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

import utils.composants.CustomPaneMorpion;

import java.rmi.RemoteException;

public class CtrlMorpion extends CtrlJeu {

    String pseudo;

    private final Image croix = new Image("Images/client/app/jeux/morpion/croix.png");
    private final Image rond = new Image("Images/client/app/jeux/morpion/rond.png");

    /*
     * Communication avec le serveur
     */
    private MorpionListener listener;
    private MorpionIF jeu;

    /*
     * Parametre de jeu
     */
    private int tailleTableau;
    private char symbole;

    /*
     * Composants FXML
     */
    @FXML private BorderPane pnl_principal;
    @FXML private Label lbl_message;

    private CustomPaneMorpion gp_morpion;

    @Override
    public void initialiser(CtrlPrincipal parent, String pseudo, JeuxListener listener) {
        this.parent = parent;
        this.pseudo = pseudo;
        this.listener = (MorpionListener) listener;
        this.listener.setControleur(this);
        this.lbl_message.setText("");
    }

    @Override
    public void setJeuxIF(JeuxIF jeuxIF) {
        this.jeu = (MorpionIF) jeuxIF;
    }

    @Override
    protected void activerComposants(boolean active) {
        if(active) lbl_message.setText("A toi de jouer!");
        else lbl_message.setText("En attente de l'adversaire...");
        this.gp_morpion.setDisable(!active);
    }

    // Fonctions de jeu.

    public void bloquerCase(int ligne, int col) {
        if(!peutJouer || !partieLance) return;

        try {
            jeu.bloquerCase(pseudo, ligne, col, symbole);
        }
        catch (RemoteException re){
            System.out.println(re.getMessage());
        }
        this.peutJouer = false;
        activerComposants(peutJouer);
    }

    /*
     * Fonctions appeler par le serveur
     */
    @Override
    public void faireJouer() {
        Platform.runLater(() -> {
            this.peutJouer = true;
            activerComposants(peutJouer);
        });
    }

    @Override
    public void arreterJeu(ResultatPartieEnum resultat, String message) {
        Platform.runLater(() ->{
            this.partieLance = false;
            activerComposants(partieLance);
            parent.getMapPane().remove("jeu");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
            alert.showAndWait();
            parent.afficher("salleattente");
        });
    }

    public void recupererParametres(int tailleTableauServeur, char symboleServeur) {
        Platform.runLater(() -> {
            tailleTableau = tailleTableauServeur;
            symbole = symboleServeur;
            this.gp_morpion = new CustomPaneMorpion(tailleTableau, tailleTableau, this);
            pnl_principal.setCenter(gp_morpion);
            activerComposants(false);
        });
    }

    public void recupererCaseBloque(int ligne, int colonne, char symbole) {
        Platform.runLater(() -> {
            System.out.println(ligne);
            System.out.println(colonne);
            gp_morpion.getMapIv().get(new Pair<Integer, Integer>(ligne, colonne)).setImage((symbole == 'X') ? croix : rond);
        });
    }
}

