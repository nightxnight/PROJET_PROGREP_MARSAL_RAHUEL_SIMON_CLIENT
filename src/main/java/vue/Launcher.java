package vue;

import vue.app.Connexion;

public class Launcher {

    /*
     * Classe permettant de lancer l'application sans passer de parametres
     * la JVM
     */

    public static void main(String[] args) {
        Connexion.launch(Connexion.class);
    }
}
