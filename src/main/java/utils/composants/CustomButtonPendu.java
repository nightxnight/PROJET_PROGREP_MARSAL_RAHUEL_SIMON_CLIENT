package utils.composants;

import controleur.app.jeux.pendu.CtrlPendu;
import javafx.scene.control.Button;

/*
 * Bouton personnalise pour afficher les lettres au pendu
 */
public class CustomButtonPendu extends Button {

    public CustomButtonPendu (char lettre, CtrlPendu controleur) {
        super();
        this.setText(String.valueOf(lettre));
        this.setPrefSize(40,40);
        this.setOnAction(actionEvent -> controleur.proposerLettre(lettre));
    }

    public void desactiver(boolean valide) {
        this.setDisable(true);
        if (valide) this.setStyle("-fx-background-color: green");
        else this.setStyle("-fx-background-color: red");
    }
}
