package controleur.app.jeux.pendu;

import controleur.app.CtrlPrincipal;
import controleur.app.jeux.CtrlJeu;
import modele.implementation.jeux.JeuxListener;
import modele.implementation.jeux.pendu.PenduListener;
import modele.serveur.stub.jeux.application.JeuxIF;
import modele.serveur.stub.jeux.application.ResultatPartieEnum;
import modele.serveur.stub.jeux.application.pendu.PenduIF;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

import utils.composants.CustomButtonPendu;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CtrlPendu extends CtrlJeu {

    private String pseudo;

    /*
     * Communication avec le serveur
     */
    private PenduListener listener;
    private PenduIF jeu;

    /*
     * Parametres de jeu
     */
    private int longueurMot;
    private char[] mot;

    /*
     * Composants FXML
     */

    @FXML private Label lbl_proposition;
    @FXML private GridPane gp_lettres;
    @FXML private TextField tf_mot;
    @FXML private ImageView iv_pendu;
    @FXML private Label lbl_mot;

    private HashMap<Character, CustomButtonPendu> mapBouton;

    @Override
    public void initialiser(CtrlPrincipal parent, String pseudo, JeuxListener listener) {
        this.parent = parent;
        this.pseudo = pseudo;
        this.listener = (PenduListener) listener;
        this.listener.setControleur(this);
        creerBoutonsLettre();
        parametrerTf_mot();
        activerComposants(false);
    }

    @Override
    public void setJeuxIF(JeuxIF jeuxIF) {
        this.jeu = (PenduIF) jeuxIF;
    }

    private void creerBoutonsLettre() {
        this.mapBouton = new HashMap<Character, CustomButtonPendu>();
        char lettre = 'A';
        for(int i = 0; i < 2; i++)
            for(int j = 0; j < 13; j++) {
                CustomButtonPendu btn_lettre = new CustomButtonPendu(lettre, this);
                mapBouton.put(lettre, btn_lettre);
                gp_lettres.add(btn_lettre, j, i);
                lettre++;
            }
    }

    private void parametrerTf_mot() {
        tf_mot.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)){
                    proposerMot();
                }
            }
        });
    }

    @Override
    protected void activerComposants(boolean active) {
        this.gp_lettres.setDisable(!active);
        this.tf_mot.setDisable(!active);
    }

    // Fonctions de jeu
    public void proposerLettre(char lettre) {
        if(!peutJouer || !partieLance) return;

        try {
            jeu.proposerLettre(pseudo, lettre);
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        }

        peutJouer = false;
        activerComposants(peutJouer);
    }

    private void proposerMot(){
        if(!peutJouer || !partieLance) return;

        String proposition = tf_mot.getText().trim().toUpperCase();
        if (proposition.equals("") || !Pattern.matches("[A-Z]+", proposition)) {
            lbl_proposition.setText("La saisie n'est pas valide.");
            lbl_proposition.setTextFill(Paint.valueOf("#E50000"));
        }

        try {
            jeu.proposerMot(pseudo, proposition);
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        }

        peutJouer = false;
        activerComposants(peutJouer);
    }

    /*
     * Fonctions appelees par le serveur
     */

    public void faireJouer() {
        Platform.runLater(() -> {
            peutJouer = true;
            activerComposants(peutJouer);
        });
    }

    public void arreterJeu(ResultatPartieEnum resultat, String message) {
        Platform.runLater(() -> {
            partieLance = false;
            activerComposants(partieLance);
            lbl_proposition.setText("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
            alert.showAndWait();
            parent.getMapPane().remove("jeu");
            parent.afficher("salleattente");
        });
    }

    public void recupererMot(char[] mot, boolean valide, String message) {
        Platform.runLater(() -> {
            lbl_mot.setText(String.valueOf(mot));
            if (valide) {
                lbl_proposition.setText(message);
                lbl_proposition.setTextFill(Paint.valueOf("#00FF00"));
            } else {
                lbl_proposition.setText(message);
                lbl_proposition.setTextFill(Paint.valueOf("#E50000"));
            }
        });
    }

    public void recupererLettrePropose(char lettre, boolean valide, String message) {
        Platform.runLater(() -> {
            mapBouton.get(lettre).desactiver(valide);
            if (valide) {
                lbl_proposition.setText(message);
                lbl_proposition.setTextFill(Paint.valueOf("#00FF00"));
            } else {
                lbl_proposition.setText(message);
                lbl_proposition.setTextFill(Paint.valueOf("#E50000"));
            }
        });
    }

    public void actualiserErreur(int nbErreur) {
        Platform.runLater(() -> {
            try {
                this.iv_pendu.setImage(new Image("/Images/client/app/jeux/pendu/imgPendu" + nbErreur + ".png"));
            } catch (Exception e) {
                System.out.println("Ressource introuvable");
            }
        });
    }
}