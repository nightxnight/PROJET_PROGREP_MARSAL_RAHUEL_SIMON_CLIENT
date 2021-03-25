package client.controleur.app.salleattente.parametresjeu;

import client.modele.serveur.stub.salleattente.SalleAttenteProprietaireIF;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CtrlParametresAllumettes extends CtrlParametresJeux implements Initializable {

    /*
     * Composants FXML
     */
    @FXML private Spinner<Integer> sp_nb_allumettes_tour;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sp_nb_allumettes_tour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1));
    }

    @Override
    public void mettreAJourComposants(SalleAttenteProprietaireIF droitsProprietaire) {
        boolean isProprietaire = droitsProprietaire != null;
        sp_nb_allumettes_tour.setDisable(!isProprietaire);
    }

    @Override
    public void changerParametre(String nom, Object valeur) {
        switch(nom) {
            case "nb_allumettes_par_tour" : sp_nb_allumettes_tour.getValueFactory().setValue((int) valeur); break;
            default : throw new IllegalArgumentException("Ce parametre n'existe pas");
        }
    }

    @Override
    public HashMap<String, Object> getMapParametres() {
        HashMap<String, Object> mapParametres = new HashMap<String, Object>();
        mapParametres.put("nb_allumettes_par_tour", sp_nb_allumettes_tour.getValue());
        return mapParametres;
    }
}
