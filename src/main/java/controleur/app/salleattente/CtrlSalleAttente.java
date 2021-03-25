package controleur.app.salleattente;

import controleur.app.CtrlPrincipal;
import controleur.app.jeux.CtrlJeu;
import controleur.app.salleattente.inviter.CtrlInviterAmis;
import controleur.app.salleattente.parametresjeu.CtrlParametresJeux;
import modele.implementation.connexion.joueur.JoueurProxy;
import modele.implementation.jeux.JeuxListener;
import modele.implementation.salleattente.ListenerSalleAttente;
import modele.serveur.stub.jeux.application.JeuxEnum;
import modele.serveur.stub.jeux.connecteur.ConnecteurJeuxIF;
import modele.serveur.stub.salleattente.SalleAttenteIF;
import modele.serveur.stub.salleattente.SalleAttenteProprietaireIF;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import utils.Paire;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CtrlSalleAttente implements Initializable {

    private CtrlPrincipal parent;
    private String pseudo;
    private Stage fenetreInvitation;

    private HashMap<String, Paire<Pane, CtrlSalleAttenteJoueurTempl>> mapJoueursPane;
    /*
     * Communication avec le serveur
     */
    private ListenerSalleAttente listener;
    private SalleAttenteIF salleAttente;
    private SalleAttenteProprietaireIF droitsProprietaire;

    /*
     * Composants FXML
     */
    @FXML private Button btn_inviter;
    @FXML private Button btn_pret;
    @FXML private Button btn_annuler;
    @FXML private VBox vBox_liste_joueur;
    @FXML private TextField tf_message;
    @FXML private ImageView btn_envoie_mess;
    @FXML private VBox vBox_chat;
    @FXML private BorderPane pnl_parametres;
    @FXML private Button btn_appliquer;
    @FXML private ChoiceBox<JeuxEnum> chbox_jeu;
    @FXML private Spinner<Integer> sp_nb_joueur;
    @FXML private PasswordField pf_mdp;
    @FXML private CheckBox cb_prive;
    @FXML private Label lbl_nom_salle;
    @FXML private Button btn_quitter;

    private Paire<Pane, CtrlParametresJeux> parametresJeu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chbox_jeu.getItems().addAll(JeuxEnum.getListeJeu());
        chbox_jeu.setConverter(new StringConverter<JeuxEnum>() {
            @Override
            public String toString(JeuxEnum jeuxEnum) {
                return jeuxEnum.getNomJeu();
            }

            @Override
            public JeuxEnum fromString(String s) {
                return JeuxEnum.fromNomJeu(s);
            }
        });
        sp_nb_joueur.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10, 1));
    }

    public void initialiser(CtrlPrincipal parent, ListenerSalleAttente listener, SalleAttenteIF salleAttente, String nomSalle, String pseudo, SalleAttenteProprietaireIF droitsProprietaire) {
        this.parent = parent;
        this.pseudo = pseudo;

        this.listener = listener;
        listener.setControleur(this);
        this.salleAttente = salleAttente;
        this.lbl_nom_salle.setText(nomSalle);
        this.droitsProprietaire = droitsProprietaire;
        this.mapJoueursPane = new HashMap<String, Paire<Pane, CtrlSalleAttenteJoueurTempl>>();
        preparerPane();
    }

    private void preparerPane() {
        try {
            actualiserJoueur();

            HashMap<String, Object> mapParametresSalle = salleAttente.getParametresSalle();
            for (String nomParametre : mapParametresSalle.keySet())
                changerParametresSalle(nomParametre, mapParametresSalle.get(nomParametre));

            creerPaneInvitation();
        } catch (ClassCastException cce) {
            System.out.println("Erreur lors du chargement des parametres de la salle.");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        mettreAJourProprietaire(this.droitsProprietaire);
    }

    private void creerPaneInvitation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/salleattente/inviteramis.fxml"));
            Pane pane = loader.load();
            CtrlInviterAmis controleur = loader.getController();
            controleur.initialiser(this.parent.getSession(), this.pseudo, salleAttente);
            Stage stage = new Stage();
            stage.setScene(new Scene(pane, 400, 400));
            stage.setTitle("INVITER DES AMIS");
            stage.centerOnScreen();
            stage.getIcons().add(new Image("/Images/client/groupeAmi.png"));
            this.fenetreInvitation = stage;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void mettreAJourProprietaire(SalleAttenteProprietaireIF droitsProprietaire) {
        this.droitsProprietaire = droitsProprietaire;
        mettreAjourPanelParametre(droitsProprietaire);
        if (parametresJeu != null) this.parametresJeu.getSecond().mettreAJourComposants(droitsProprietaire);
    }

    private void mettreAjourPanelParametre(SalleAttenteProprietaireIF droitsProprietaire) {
        boolean isProprietaire = droitsProprietaire != null;
        chbox_jeu.setDisable(!isProprietaire);
        sp_nb_joueur.setDisable(!isProprietaire);
        pf_mdp.setDisable(!isProprietaire);
        cb_prive.setDisable(!isProprietaire);
        btn_appliquer.setDisable(!isProprietaire);
    }

    public void connexionJoueur(JoueurProxy joueur, boolean proprietaire, boolean active, boolean pret) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/app/salleattente/template_joueur_salleattente.fxml"));
            Pane pane = loader.load();
            CtrlSalleAttenteJoueurTempl controleur = loader.getController();
            controleur.initialiser(this, joueur, proprietaire, active, pret);
            vBox_liste_joueur.getChildren().add(pane);
            mapJoueursPane.put(joueur.getPseudo(), new Paire<Pane, CtrlSalleAttenteJoueurTempl>(pane, controleur));
        } catch (Exception e) {
            // Erreur FXML : should not occurs
        }
    }

    private void changerJeu(String nomJeu) {
        chbox_jeu.getSelectionModel().select(JeuxEnum.fromNomJeu(nomJeu));
        String url = "/fxml/client/app/salleattente/parametresjeux/";
        if(nomJeu.equals(JeuxEnum.MORPION.getNomJeu())) url += "parametres_morpion.fxml";
        else if(nomJeu.equals(JeuxEnum.PENDU.getNomJeu())) url += "parametres_pendu.fxml";
        else if(nomJeu.equals(JeuxEnum.ALLUMETTES.getNomJeu())) url += "parametres_allumettes.fxml";
        else throw new IllegalArgumentException("Aucun jeu de ce nom n'existe.");

        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
        try {
            Pane pane = loader.load();
            CtrlParametresJeux controleur = loader.getController();
            controleur.initialiser(this, salleAttente.getParametresJeu(), droitsProprietaire);
            this.parametresJeu = new Paire<Pane, CtrlParametresJeux>(pane, controleur);
            this.pnl_parametres.setCenter(pane);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /*
     * Methodes communes
     */

    @FXML
    private void quitter_salle(MouseEvent event) {
        try {
            salleAttente.sortir(pseudo);
            parent.getMapPane().remove("salleattente");
            parent.afficher("liste_salleattente");
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        }
    }

    @FXML
    private void envoyerMessage(MouseEvent event) {
        String message = tf_message.getText().trim();
        try {
            if (!message.equals("")) {
                salleAttente.envoyerMessage(pseudo, message);
                tf_message.clear();
            }
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        }
    }

    @FXML
    private void changerEtat(MouseEvent event) {
        try {
            boolean pret = event.getSource().equals(btn_pret);
            salleAttente.changerEtat(pseudo, pret);
            if (pret) mettreAjourPanelParametre(null); else mettreAjourPanelParametre(this.droitsProprietaire);
            btn_pret.setVisible(!pret); btn_annuler.setVisible(pret);
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        }
    }

    @FXML
    private void inviter(MouseEvent event) {
        this.fenetreInvitation.showAndWait();
    }

    /*
     * Methodes proprietaire
     */

    public void exclure(String pseudoAExclure) {
        try {
            droitsProprietaire.virerJoueur(this.pseudo, pseudoAExclure);
        } catch (RemoteException re) {
            System.out.println(re.getMessage());
        }
    }

    public void nommerProprietaire(String pseudoNouveauProprietaire) {
        try {
            droitsProprietaire.nommerProprietaire(this.pseudo, pseudoNouveauProprietaire);
            mettreAJourProprietaire(null);
        } catch(RemoteException re ) {
            System.out.println(re.getMessage());
        }
    }

    @FXML
    void appliquerChangements(MouseEvent event) throws RemoteException {
        String jeu = chbox_jeu.getValue().getNomJeu();
        int nbJoueursMax = sp_nb_joueur.getValue();
        String mdp = pf_mdp.getText().trim();
        boolean prive = cb_prive.isSelected();
        boolean chat = true;
        String[] nomParametres = {"jeu", "nombre_joueur", "mot_de_passe", "chat", "publique"};
        Object[] valeurs = {jeu, nbJoueursMax, mdp, chat, !prive};
        for(int i = 0; i < nomParametres.length; i ++)
            droitsProprietaire.changerParametreSalle(this.pseudo, nomParametres[i], valeurs[i]);
        HashMap<String, Object> mapParametres = parametresJeu.getSecond().getMapParametres();
        for(String nom : mapParametres.keySet())
            try {
                droitsProprietaire.changerParametreJeu(this.pseudo, nom, mapParametres.get(nom));
            } catch(Exception e) {
                break; // Le jeu a change
            }
    }

    /*
     * Methodes appelees par le serveur
     */

    public void actualiserJoueur() {
        Platform.runLater(() -> {
            try {
                vBox_liste_joueur.getChildren().clear();
                String proprietaire = salleAttente.whoIsProprietaire();
                HashMap<JoueurProxy, Boolean> mapJoueurs = salleAttente.getListeJoueur();
                for (JoueurProxy joueur : mapJoueurs.keySet())
                    connexionJoueur(joueur, joueur.getPseudo().equals(proprietaire), this.droitsProprietaire != null && !this.pseudo.equals(joueur.getPseudo()), mapJoueurs.get(joueur));
            } catch (RemoteException re) {
                // should not occur.
            }
        });
    }


    public void designerProprietaire(SalleAttenteProprietaireIF droitsProprietaire) {
        Platform.runLater(() -> {
            mettreAJourProprietaire(droitsProprietaire);
        });
    }

    public void exclure() {
        Platform.runLater(() -> {
            parent.getMapPane().remove("salleattente");
            parent.afficher("liste_salleattente");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Vous avez ete exclue.", ButtonType.OK);
            alert.setTitle("Desole");
            alert.showAndWait();
        });
    }


    public void connexionJoueur(JoueurProxy joueur) {
        Platform.runLater(() -> {
            try {
                connexionJoueur(joueur, false, this.droitsProprietaire != null, false);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public void deconnexionJoueur(String pseudo) {
        Platform.runLater(() -> {
            vBox_liste_joueur.getChildren().remove(mapJoueursPane.get(pseudo).getPremier());
            mapJoueursPane.remove(pseudo);
        });
    }

    public void afficherMessage(String message) {
        Platform.runLater(() -> {
            vBox_chat.getChildren().add(new Label(message));
        });
    }

    public void activerPret(boolean active) {
        Platform.runLater(() -> {
            btn_pret.setDisable(!active); btn_annuler.setDisable(!active);
            if(!active)
                btn_pret.setVisible(true); btn_annuler.setVisible(false);
                for(String pseudo : mapJoueursPane.keySet())
                    mapJoueursPane.get(pseudo).getSecond().changerEtat(false);
        });
    }

    public void changementEtat(String pseudoJoueur, boolean etat) {
        Platform.runLater(() -> {
            mapJoueursPane.get(pseudoJoueur).getSecond().changerEtat(etat);
        });
    }

    public void changerParametresSalle(String nom, Object valeur) throws ClassCastException, IllegalArgumentException{
        Platform.runLater(() -> {
            switch(nom) {
                case "nombre_joueur" :
                    sp_nb_joueur.getValueFactory().setValue((int) valeur);
                    break;
                case "mot_de_passe" :
                    pf_mdp.setText((String) valeur);
                    break;
                case "chat" :
                    tf_message.setDisable(!((boolean) valeur));
                    btn_envoie_mess.setDisable(!((boolean) valeur));
                    break;
                case "publique" :
                    cb_prive.setSelected(!((boolean) valeur));
                    break;
                case "jeu" :
                    changerJeu((String) valeur);
                    break;
                case "pret":
                    activerPret((boolean) valeur);
                    break;
                default : throw new IllegalArgumentException("Il n'existe pas de parametre de ce nom");
            }
        });
    }

    public void changerParametresJeu(String nom, Object valeur) {
        Platform.runLater(() -> {
            parametresJeu.getSecond().changerParametre(nom, valeur);
        });
    }

    public void rejoindrePartie(ConnecteurJeuxIF connecteur, JeuxEnum jeu) {
        Platform.runLater(() -> {
            try {
                JeuxListener listener = JeuxListener.creerFromJeu(jeu);

                String url = "/fxml/client/app/jeux/";
                switch(jeu) {
                    case MORPION:
                        url += "morpion/morpion.fxml";
                        break;
                    case ALLUMETTES:
                        url += "allumettes/allumettes.fxml";
                        break;
                    case PENDU:
                        url += "pendu/pendu.fxml";
                        break;
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
                Pane pane = loader.load();
                CtrlJeu controleur = loader.getController();
                controleur.initialiser(this.parent, pseudo, listener);
                controleur.setJeuxIF(connecteur.rejoindrePartie(pseudo, listener));
                parent.getMapPane().put("jeu", pane);
                parent.afficher("jeu");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
    /*
     * Getter
     */

    public CtrlPrincipal getParent() {
        return parent;
    }

    public String getPseudo() {
        return pseudo;
    }
}
