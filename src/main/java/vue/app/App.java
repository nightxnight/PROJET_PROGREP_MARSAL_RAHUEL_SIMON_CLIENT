package vue.app;

import controleur.app.CtrlPrincipal;
import modele.serveur.stub.connexion.session.SessionIF;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.rmi.RemoteException;

public class App extends Application {

    private String pseudo;
    private SessionIF session;

    public App(SessionIF session, String pseudo) throws Exception {
        this.session = session;
        this.pseudo = pseudo = pseudo;
        start(new Stage());
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/fenetre_principale.fxml"));
            Parent root = loader.load();
            CtrlPrincipal controleur = loader.getController();
            controleur.initialiser(this.pseudo, this.session);
            stage.setTitle("NOBÉLIUM");
            stage.setScene(new Scene(root, 1100, 830));
            stage.centerOnScreen();
            stage.getIcons().add(new Image("Images/client/application_icon_taskbar.png"));
            stage.setMinWidth(1100);
            stage.setMinHeight(830);
            stage.setOnCloseRequest(Event -> {
                try {
                    this.session.logout();
                } catch (RemoteException re) {
                    re.printStackTrace();
                }
                System.exit(1);
            });
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
