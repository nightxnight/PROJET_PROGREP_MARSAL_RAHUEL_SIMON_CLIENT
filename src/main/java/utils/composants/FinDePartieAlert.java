package utils.composants;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modele.serveur.stub.jeux.application.ResultatPartieEnum;

/*
 * Alerte de fin de partie personnalise
 */
public class FinDePartieAlert extends Alert {

    private Image gagne = new Image("./Images/client/app/alert/gagne.png");
    private Image egalite = new Image("./Images/client/app/alert/egalite.png");
    private  Image perdu = new Image("./Images/client/app/alert/perdu.png");

    public FinDePartieAlert(ResultatPartieEnum resultat, String message) {
        super(AlertType.INFORMATION, message, ButtonType.OK);
        this.setTitle("Fin de partie!");

        if (resultat == ResultatPartieEnum.GAGNE || resultat == ResultatPartieEnum.GAGNANT_PAR_FORFAIT) {
            this.setHeaderText("VICTOIRE !");
            parametrerImage(gagne);
        } else if (resultat == ResultatPartieEnum.EGALITE) {
            this.setHeaderText("Egalite.");
            parametrerImage(egalite);
        }
        else {
            this.setHeaderText("perdu...");
            parametrerImage(perdu);
        }


        this.getDialogPane().getStylesheets().add("./css/Main.css");
    }

    private void parametrerImage(Image image) {
        this.setGraphic(new ImageView(image));
        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(image);
        stage.centerOnScreen();
    }
}
