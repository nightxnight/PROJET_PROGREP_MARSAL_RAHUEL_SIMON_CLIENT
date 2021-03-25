package client.controleur.app.salleattente.parametresjeu;

import client.modele.serveur.stub.salleattente.SalleAttenteProprietaireIF;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CtrlParametresMorpion extends CtrlParametresJeux implements Initializable {


    /*
     * Composants FXML
     */
    @FXML private Spinner<Integer> sp_taille_tableau;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sp_taille_tableau.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 5, 1));
    }

    @Override
    public void mettreAJourComposants(SalleAttenteProprietaireIF droitsProprietaire) {
        boolean isProprietaire = droitsProprietaire != null;
        sp_taille_tableau.setDisable(!isProprietaire);
    }

    @Override
    public void changerParametre(String nom, Object valeur) throws ClassCastException, IllegalArgumentException {
        switch(nom) {
            case "taille_tableau" : sp_taille_tableau.getValueFactory().setValue((int) valeur); break;
            default : throw new IllegalArgumentException("Ce parametre n'existe pas");
        }
    }

    @Override
    public HashMap<String, Object> getMapParametres() {
        HashMap<String, Object> mapParametres = new HashMap<String, Object>();
        mapParametres.put("taille_tableau", sp_taille_tableau.getValue());
        return mapParametres;
    }

}
