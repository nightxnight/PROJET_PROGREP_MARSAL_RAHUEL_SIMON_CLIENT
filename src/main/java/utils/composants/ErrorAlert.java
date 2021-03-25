package utils.composants;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ErrorAlert extends Alert {

    public ErrorAlert(String message) {
        super(AlertType.ERROR, message, ButtonType.OK);
        this.setTitle("Petit probleme");
        Image imageErreur = new Image("./Images/client/app/alert/error.png");
        this.setGraphic(new ImageView(imageErreur));
        this.getDialogPane().getStylesheets().add("./css/Main.css");

        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(imageErreur);
        stage.centerOnScreen();
    }

}