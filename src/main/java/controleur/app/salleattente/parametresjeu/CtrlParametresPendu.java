package controleur.app.salleattente.parametresjeu;

import modele.serveur.stub.salleattente.SalleAttenteProprietaireIF;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/*
 * controleur des parametres du pendu
 */
public class CtrlParametresPendu extends CtrlParametresJeux implements Initializable {

    /*
     * Composants FXML
     */
    @FXML private ChoiceBox<String> chbox_langue;
    @FXML private CheckBox cb_erreur;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<String> listeLangue = new ArrayList<String>();
        listeLangue.add("Francais");
        listeLangue.add("Anglais");
        listeLangue.add("Allemand");
        chbox_langue.getItems().addAll(listeLangue);
    }

    @Override
    public void mettreAJourComposants(SalleAttenteProprietaireIF droitsProprietaire) {
        boolean isProprietaire = droitsProprietaire != null;
        chbox_langue.setDisable(!isProprietaire);
        cb_erreur.setDisable(!isProprietaire);
    }

    @Override
    public void changerParametre(String nom, Object valeur) throws ClassCastException, IllegalArgumentException {
        switch(nom) {
            case "langue" : chbox_langue.getSelectionModel().select((String) valeur); break;
            case "erreur_active" : cb_erreur.setSelected((boolean) valeur); break;
            default : throw new IllegalArgumentException("Ce parametre n'existe pas");
        }
    }

    @Override
    public HashMap<String, Object> getMapParametres() {
        HashMap<String, Object> mapParametres = new HashMap<String, Object>();
        mapParametres.put("langue", chbox_langue.getValue());
        mapParametres.put("erreur_active", cb_erreur.isSelected());
        return mapParametres;
    }
}
