package utils.composants;

import controleur.app.jeux.morpion.CtrlMorpion;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Pair;

import java.util.HashMap;

/*
 * Panel personnalise permettant de genere un morpion de taille x
 */
public class CustomPaneMorpion extends GridPane {

    private CtrlMorpion controleur;

    private HashMap<Pair<Integer, Integer>, ImageView> mapIv;

    public CustomPaneMorpion(int ligne, int col, CtrlMorpion controleur) {
        super();
        this.controleur = controleur;
        this.mapIv = new HashMap<Pair<Integer, Integer>, ImageView>();

        this.setWidth(USE_COMPUTED_SIZE);
        this.setHeight(USE_COMPUTED_SIZE);
        this.setAlignment(Pos.CENTER);
        this.setGridLinesVisible(true);

        for (int i = 0; i < ligne; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setValignment(VPos.CENTER);
            rowConst.setMinHeight(USE_COMPUTED_SIZE);
            rowConst.setPrefHeight(USE_COMPUTED_SIZE);
            rowConst.setMaxHeight(USE_COMPUTED_SIZE);
            this.getRowConstraints().add(rowConst);
        }
        for (int j = 0; j < col; j++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setHalignment(HPos.CENTER);
            colConst.setMinWidth(USE_COMPUTED_SIZE);
            colConst.setPrefWidth(USE_COMPUTED_SIZE);
            colConst.setMaxWidth(USE_COMPUTED_SIZE);
            this.getColumnConstraints().add(colConst);
        }
        ajouterImageView(ligne, col);
    }

    private void ajouterImageView(int ligne, int col) {
        for(int i = 0; i < ligne; i++)
            for(int j = 0; j < col; j++) {
                ImageView iv = new ImageView();
                iv.setPreserveRatio(true);
                iv.setFitHeight(100);
                iv.setImage(new Image("/Images/client/app/jeux/morpion/libre.png", 100, 100, false, true));
                iv.setCursor(Cursor.HAND);
                int idxLigne = i;
                int idxColonne = j;
                mapIv.put(new Pair<Integer, Integer>(idxLigne, idxColonne), iv);
                iv.setOnMouseClicked(EventHandler -> {
                    bloquerCase(idxLigne, idxColonne);
                });
                this.add(iv, j, i);
            }
    }

    private void bloquerCase(int ligne, int colonne) {
        controleur.bloquerCase(ligne, colonne);
    }

    public HashMap<Pair<Integer, Integer>, ImageView> getMapIv() {
        return mapIv;
    }
}
