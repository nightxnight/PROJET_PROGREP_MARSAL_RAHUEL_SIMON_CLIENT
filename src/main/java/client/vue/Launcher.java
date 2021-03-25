package client.vue;

import client.vue.app.Connexion;

public class Launcher {

    /*
     * Classe qui permettera de lancer l'application
     */

    public static void main(String[] args) {
        Connexion.launch(Connexion.class);
    }
}
