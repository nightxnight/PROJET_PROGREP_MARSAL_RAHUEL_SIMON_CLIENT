package utils.composants;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/*
 * Code par drguildo, lien github : https://github.com/drguildo
 * ->
 * Permet de creer une fenetre de dialog destine a la saisie de mot de passe.
 *
 */

public class CustomPasswordDialog extends Dialog<String> {

    private PasswordField passwordField;

    public CustomPasswordDialog(String message) {
        this.setTitle("Mot de passe");
        this.setHeaderText(message);

        ButtonType passwordButtonType = new ButtonType("Entrer", ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(passwordButtonType, ButtonType.CANCEL);

        passwordField = new PasswordField();
        passwordField.setPromptText("mot de passe");

        HBox hBox = new HBox();
        hBox.getChildren().add(passwordField);
        hBox.setPadding(new Insets(20));
        hBox.setMaxWidth(50);
        hBox.setPrefHeight(25);

        getDialogPane().setContent(hBox);

        Platform.runLater(() -> passwordField.requestFocus());

        setResultConverter(dialogButton -> {
            if (dialogButton == passwordButtonType) {
                return passwordField.getText();
            }
            return null;
        });

        this.getDialogPane().getStylesheets().add("./css/Main.css");
        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("./Images/client/app/salleattente/Locked.png"));
        stage.centerOnScreen();
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }
}
