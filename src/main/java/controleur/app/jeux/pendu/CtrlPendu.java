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
import utils.composants.FinDePartieAlert;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.regex.Pattern;

/*
 * controleur du jeu du pendu
 */
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

    private HashMap<Character, CustomButtonPendu> mapBouton; // Permet de stocker les boutons indexes par leur lettre correspondante

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

    /*
     * Cree les boutons pour chaque lettre de l'alphabet
     */
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
    /*
     * Permet de proposer une lettre au serveur
     * Le label de proposition se mettera a jour si une erreur est leve (Pas a ce joueur de jouer)
     */
    public void proposerLettre(char lettre) {
        if(!peutJouer || !partieLance) return;
        lbl_proposition.setTextFill(Paint.valueOf("#000000"));
        try {
            jeu.proposerLettre(pseudo, lettre);
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        } catch (IllegalArgumentException iae) {
            lbl_proposition.setTextFill(Paint.valueOf("#E50000"));
            lbl_proposition.setText(iae.getMessage());
            return;
        }

        peutJouer = false;
        activerComposants(peutJouer);
    }

    /*
     * Propose un mot au serveur, on ne peut pas proposer le mot vide.
     * Le label de proposition est mis a jour si une erreur est leve.
     */
    private void proposerMot(){
        if(!peutJouer || !partieLance) return;

        lbl_proposition.setTextFill(Paint.valueOf("#000000"));
        String proposition = tf_mot.getText().trim().toUpperCase();
        if (proposition.equals("") || !Pattern.matches("[A-Z]+", proposition)) {
            lbl_proposition.setText("La saisie n'est pas valide.");
            lbl_proposition.setTextFill(Paint.valueOf("#E50000"));
        }

        try {
            jeu.proposerMot(pseudo, proposition);
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        } catch (IllegalArgumentException iae) {
            lbl_proposition.setTextFill(Paint.valueOf("#E50000"));
            lbl_proposition.setText(iae.getMessage());
            return;
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
            lbl_proposition.setText("C'est finit!");
            parent.getMapPane().remove("jeu");
            new FinDePartieAlert(resultat, message).showAndWait();
            parent.afficher("salleattente");
        });
    }

    /*
     * Recupere le mot a jour si une lettre a ete trouve, indique qui l'a propose
     */
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

    /*
     * Desactive un bouton de lettre si celle ci a ete propose, indique qu'il l'a propose
     */
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

    /*
     * Actualise l'image du pendu si un joueur commet une erreur
     */
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