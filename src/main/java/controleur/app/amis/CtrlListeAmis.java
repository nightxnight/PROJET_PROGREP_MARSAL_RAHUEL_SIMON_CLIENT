package controleur.app.amis;

import controleur.app.CtrlPrincipal;
import controleur.app.amis.chat.CtrlChatAmis;
import controleur.app.amis.template.CtrlTemplateAmis;
import modele.implementation.amis.AmisListener;
import modele.client.stub.amis.AmisListenerIF;
import modele.implementation.amis.chat.Message;
import modele.implementation.connexion.joueur.JoueurProxy;
import modele.serveur.stub.amis.PortailAmisIF;
import modele.serveur.stub.connexion.session.SessionIF;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utils.Paire;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/*
 * Permet de gerer la liste des amis, d'ouvrir le chat correspondant
 * les supprimer de la liste, etc, ...
 */
public class CtrlListeAmis extends CtrlAmis {

    private CtrlPrincipal parent;
    private String pseudo;
    private PortailAmisIF portailAmis;
    private AmisListener listener;

    private HashMap<String, Paire<Pane, CtrlAmis>> mapPane;
    private HashMap<String, Paire<Pane, CtrlTemplateAmis>> mapTemplateAmis;
    private HashMap<String, Paire<Pane, CtrlChatAmis>> mapChat;

    /*
     * Composants FXML
     */
    @FXML private BorderPane root;
    @FXML private TextField tf_rechercher;
    @FXML private ImageView btn_rechercher;
    @FXML private BorderPane pnl_liste;
    @FXML private ImageView btn_mesamis;
    @FXML private ImageView btn_demandes;
    @FXML private Label lbl_nb_demandes;
    @FXML private VBox vbox_mesamis;
    @FXML private Label lbl_vide;

    public void initialiser(CtrlPrincipal parent, String pseudo, SessionIF session) {
        this.parent = parent;
        this.pseudo = pseudo;
        this.mapPane = new HashMap<String, Paire<Pane, CtrlAmis>>();
        this.mapTemplateAmis = new HashMap<String, Paire<Pane, CtrlTemplateAmis>>();
        this.mapChat = new HashMap<String, Paire<Pane, CtrlChatAmis>>();

        try {
            this.listener = new AmisListener();
            this.portailAmis = session.getPortailsAmis((AmisListenerIF) listener);
            initialiserComposants();
            this.listener.setControleurs(this, mapPane.get("demandeAmis").getSecond());
            initialiserListe(portailAmis.getListeAmis(this.pseudo));
        } catch (RemoteException re) { System.out.println(re.getMessage()); }
    }

    private void initialiserComposants() {
        mapPane.put("mesamis", new Paire<Pane, CtrlAmis>(vbox_mesamis, this));
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/amis/demande_amis.fxml"));
            Pane pane  = loader.load();
            CtrlDemandeAmis controleur = loader.getController();
            controleur.initialiser(this, this.pseudo, this.portailAmis);
            mapPane.put("demandeAmis", new Paire<Pane, CtrlAmis>(pane, controleur));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void initialiserListe(ArrayList<JoueurProxy> listeAmis) {
        vbox_mesamis.getChildren().clear();
        vbox_mesamis.getChildren().add(lbl_vide);
        for(JoueurProxy ami : listeAmis)
            ajouterAmis(ami);
    }

    /*
     * Ouvre le chat correspondant a un amis sur le bord droit
     */
    private void creerChat(JoueurProxy joueur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/amis/amis_chat.fxml"));
            Pane pane = loader.load();
            CtrlChatAmis controleur = loader.getController();
            controleur.initialiser(this, this.pseudo, joueur, this.portailAmis);
            mapChat.put(joueur.getPseudo(), new Paire<Pane, CtrlChatAmis>(pane, controleur));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Permet de basculer entre la liste des amis / liste des demandes
     */
    @FXML
    void changerPage(MouseEvent event) {
        if(event.getSource().equals(btn_mesamis)) {
            this.pnl_liste.setCenter(mapPane.get("mesamis").getPremier());
        } else {
            this.pnl_liste.setCenter(mapPane.get("demandeAmis").getPremier());
        }
    }

    /*
     * Effectue une recherche des amis
     */
    @FXML
    void rechercherAmis(MouseEvent event) {
        try {
            ArrayList<JoueurProxy> listeAmis = portailAmis.getListeAmis(this.pseudo);
            initialiserListe(new ArrayList<>(listeAmis.stream().filter(joueurProxy -> joueurProxy.getPseudo().startsWith(tf_rechercher.getText())).collect(Collectors.toList())));
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        }
    }

    /*
     * Fonctions appeles par le serveur
     */

    /*
     * Permet de recuperer un amis directement si une demande est accepter.
     */
    public void ajouterAmis(JoueurProxy joueurProxy) {
        Platform.runLater(() -> {
            lbl_vide.setVisible(false);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/amis/template_amis.fxml"));
                Pane pane = loader.load();
                CtrlTemplateAmis controleur = loader.getController();
                controleur.initialiser(this, this.pseudo, joueurProxy, this.portailAmis);
                vbox_mesamis.getChildren().add(pane);
                mapTemplateAmis.put(joueurProxy.getPseudo(), new Paire<Pane, CtrlTemplateAmis>(pane, controleur));
                creerChat(joueurProxy);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    /*
     * Permet de faire supprimer un amis de la liste si celui ci / vous, le supprime
     */
    public void supprimerAmis(JoueurProxy joueurProxy) {
        Platform.runLater(() -> {
            try {
                vbox_mesamis.getChildren().remove(mapTemplateAmis.get(joueurProxy.getPseudo()).getPremier());
                mapTemplateAmis.remove(joueurProxy.getPseudo());
                if(vbox_mesamis.getChildren().size() == 1) lbl_vide.setVisible(true);
            } catch (Exception e) {
                // Ne rien faire;
            }
        });
    }

    /*
     * Permet de transferer un message directement a un chat
     * Le type de message est traiter dans le controleur de chat prive.
     */
    public void recupererMessage(String de, Message message) {
        Platform.runLater(() -> {
            mapChat.get(de).getSecond().recupererMessage(message);
        });
    }

    /*
     * Met a jour l'etat d'un amis, c'est a dire si il est en ligne / hors ligne
     */
    public void actualiserEtat(String joueur, boolean etat) {
        Platform.runLater(() -> {
            mapTemplateAmis.get(joueur).getSecond().actualiserEtat(etat);
        });
    }

    /*
     * Getters
     */

    public CtrlPrincipal getParent() {
        return parent;
    }

    public HashMap<String, Paire<Pane, CtrlAmis>> getMapPane() {
        return mapPane;
    }

    public HashMap<String, Paire<Pane, CtrlChatAmis>> getMapChat() {
        return mapChat;
    }

    public BorderPane getRoot() {
        return root;
    }

    public Label getLbl_nb_demandes() {
        return lbl_nb_demandes;
    }
}