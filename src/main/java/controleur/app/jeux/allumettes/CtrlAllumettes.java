package controleur.app.jeux.allumettes;

import controleur.app.CtrlPrincipal;
import controleur.app.jeux.CtrlJeu;
import javafx.scene.paint.Paint;
import modele.implementation.jeux.JeuxListener;
import modele.implementation.jeux.allumettes.AllumettesListener;
import modele.serveur.stub.jeux.application.JeuxIF;
import modele.serveur.stub.jeux.application.ResultatPartieEnum;
import modele.serveur.stub.jeux.application.allumettes.AllumettesIF;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import utils.composants.FinDePartieAlert;

import java.rmi.RemoteException;

/*
 * Controleur associe au jeu des allumettes
 */
public class CtrlAllumettes extends CtrlJeu {

    private String pseudo;

    /*
     * Communication avec le serveur
     */
    private AllumettesListener listener = null;
    private AllumettesIF jeu;

    /*
     * Parametres de jeu
     */
    private int nbAllumettesInitial;
    private int nbAllumettesPrises;

    /*
     * Composants FXML
     */
    @FXML private FlowPane pnl_allumettes;
    @FXML private Spinner<Integer> spinner_nb_allumettes;
    @FXML private Button btn_prendre;
    @FXML private Label lbl_pris;
    @FXML private Label lbl_nb_allumettes;

    private Image img_allumettes = new Image("Images/client/app/jeux/allumettes/allumette.png");

    @Override
    public void initialiser(CtrlPrincipal parent, String pseudo, JeuxListener listener) {
        this.parent = parent;
        this.pseudo = pseudo;
        this.listener = (AllumettesListener) listener;
        this.listener.setControleur(this);
        activerComposants(false);
    }

    @Override
    public void setJeuxIF(JeuxIF jeuxIF) {
        this.jeu = (AllumettesIF) jeuxIF;
    }

    /*
     * Affiche un nombre x d'allumettes en fonction du nombre
     * choisit par le serveur
     */
    private void creerAllumettes(int nbAllumettes) {
        for(int i = 0; i < nbAllumettes; i++) {
            ImageView iv = new ImageView(img_allumettes);
            iv.setFitHeight(50);
            iv.setPreserveRatio(true);
            pnl_allumettes.getChildren().add(iv);
        }
        this.nbAllumettesPrises = 0;
        lbl_nb_allumettes.setText(String.valueOf(nbAllumettesPrises));
    }

    @Override
    protected void activerComposants(boolean active) {
        spinner_nb_allumettes.setDisable(!active);
        btn_prendre.setDisable(!active);
    }

    // Fonctions de jeu
    /*
     * Demande au serveur de retirer x allumettes.
     * spinner_nb_allumettes contient la valeur qui sera retirer.
     */
    @FXML
    private void prendre_allumettes(MouseEvent event) {
        if(!peutJouer || !partieLance) return;

        int nbAllumettes = spinner_nb_allumettes.getValue();

        try {
            jeu.prendreAllumettes(pseudo, nbAllumettes);
            lbl_pris.setTextFill(Paint.valueOf("#000000"));
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        } catch (IllegalArgumentException iae) {
            lbl_pris.setTextFill(Paint.valueOf("#FF0000"));
            lbl_pris.setText(iae.getMessage());
            return;
        }

        peutJouer = false;
        activerComposants(false);
    }

    /*
     * Fonctions appelees par le serveur
     */

    /*
     * Permet de recuperer les parametres de jeu
     */
    public void recupererParametres(int nbAllumettesInitial, int nbAllumettesMaxTour) {
        Platform.runLater(() -> {
            this.nbAllumettesInitial = nbAllumettesInitial;
            this.spinner_nb_allumettes.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, nbAllumettesMaxTour, 1));
            creerAllumettes(this.nbAllumettesInitial);
        });
    }

    /*
     * Actualise le nombre d'allumettes retirable max si
     * le nombre d'allumettes retirable en 1 tour < nombre d'allumettes restante
     */
    public void actualiserNombreMaxAllumettesTour(int nombre) {
        Platform.runLater(() -> {
            this.spinner_nb_allumettes.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, nombre, 1));
        });
    }

    /*
     * Retire un nombre x d'allumettes des allumettes restantes et
     * indique aux joueurs qui a pris les allumettes
     */
    public void retirerAllumettes(String pseudoPreneur, int nbAllumettes, String message) {
        Platform.runLater(() -> {
            for(int i = 1; i <= nbAllumettes; i++) {
                pnl_allumettes.getChildren().remove(0);
            }
            lbl_pris.setText(message);
            if(this.pseudo.equals(pseudoPreneur)) {
                this.nbAllumettesPrises += nbAllumettes;
                lbl_nb_allumettes.setText(String.valueOf(this.nbAllumettesPrises));
            }
        });
    }

    @Override
    public void faireJouer() {
        Platform.runLater(() ->{
            peutJouer = true;
            activerComposants(peutJouer);
        });

    }

    @Override
    public void arreterJeu(ResultatPartieEnum resultat, String message) {
        Platform.runLater(() -> {
            partieLance = false;
            activerComposants(partieLance);
            parent.getMapPane().remove("jeu");
            new FinDePartieAlert(resultat, message).showAndWait();
            parent.afficher("salleattente");
        });
    }
}
