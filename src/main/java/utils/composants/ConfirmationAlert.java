package utils.composants;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/*
 * Alerte de confirmation personnalise
 */
public class ConfirmationAlert extends Alert {

    private final ButtonType VALIDER = new ButtonType("Valider");
    private final ButtonType ANNULER = new ButtonType("Annuler");

    public ConfirmationAlert(String message) {
        super(AlertType.CONFIRMATION, message);
        this.setTitle("confirmation");
        this.getButtonTypes().clear();
        this.getButtonTypes().addAll(VALIDER, ANNULER);

        Image confirmationImage = new Image("./Images/client/app/alert/confirmation.png");
        this.setGraphic(new ImageView(confirmationImage));
        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(confirmationImage);
        stage.centerOnScreen();

        this.getDialogPane().getStylesheets().add("./css/Main.css");
    }

    public ButtonType getVALIDER() {
        return VALIDER;
    }

    public ButtonType getANNULER() {
        return ANNULER;
    }
}
