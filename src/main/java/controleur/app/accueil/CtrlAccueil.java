package controleur.app.accueil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CtrlAccueil {

    @FXML private Label lbl_pseudo;

    public void initialiser(String pseudo) {
        this.lbl_pseudo.setText(pseudo);
    }
}
