package vue.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import modele.serveur.stub.jeux.application.ResultatPartieEnum;
import utils.composants.ConfirmationAlert;
import utils.composants.ErrorAlert;
import utils.composants.FinDePartieAlert;

import java.rmi.RemoteException;

public class Connexion extends Application {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 6000;

    /*
     * Classe qui permettera d'initialiser la fenetre principale
     * charger la configuration utilisateur si elle existe sinon
     * elle la créera. Elle se connectera au serveur, etc ...
     */
    public Connexion() {
        super();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/client/app/connexion/connexion.fxml"));
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("CONNEXION");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.getIcons().add(new Image("Images/client/connexion_icon_taskbar.png"));
        stage.setResizable(false);
        stage.setOnCloseRequest(Event -> {
            System.exit(1);
        });
        stage.show();
    }
}
