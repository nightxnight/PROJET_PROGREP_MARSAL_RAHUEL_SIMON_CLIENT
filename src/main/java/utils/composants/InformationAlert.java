package utils.composants;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class InformationAlert extends Alert {

    public InformationAlert(String message) {
        super(AlertType.INFORMATION, message, ButtonType.OK);
        this.setTitle("Information");
        this.setHeaderText("Information");
        Image imageInfo = new Image("./Images/client/app/alert/info.png");
        this.setGraphic(new ImageView(imageInfo));
        this.getDialogPane().getStylesheets().add("./css/Main.css");

        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(imageInfo);
        stage.centerOnScreen();
    }
}
