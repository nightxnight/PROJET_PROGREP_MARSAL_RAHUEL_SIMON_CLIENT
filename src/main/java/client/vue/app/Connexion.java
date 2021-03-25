package client.vue.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Connexion extends Application {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 6000;

    /*
     * Classe qui permettera d'initialiser la fenetre principale
     * charger la configuration utilisateur si elle existe sinon
     * elle la créera. Elle se connectera au serveur, etc ...
     */

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/client/app/connexion/connexion.fxml"));
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("CONNEXION");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.getIcons().add(new Image("Images/client/connexion_icon_taskbar.png"));
        stage.setResizable(false);
        stage.show();
    }
}
