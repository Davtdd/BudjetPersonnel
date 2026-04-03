package ui;

import dao.UtilisateurDAO;
import database.DatabaseConnection;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Utilisateur;

import java.sql.Connection;

public class LoginView extends VBox {

    // Boutons de sélection
    private final ToggleGroup choixGroup = new ToggleGroup();
    private final RadioButton radioInscription = new RadioButton("Inscription");
    private final RadioButton radioConnexion = new RadioButton("Connexion");

    // Panneaux dynamiques
    private final VBox panelInscription = new VBox(10);
    private final VBox panelConnexion = new VBox(10);

    // Champs inscription
    private final TextField nomField = new TextField();
    private final TextField prenomField = new TextField();
    private final TextField emailFieldInscription = new TextField();
    private final PasswordField mdpFieldInscription = new PasswordField();
    private final Button btnInscrire = new Button("S'inscrire");

    // Champs connexion
    private final TextField emailFieldConnexion = new TextField();
    private final PasswordField mdpFieldConnexion = new PasswordField();
    private final Button btnConnexion = new Button("Se connecter");

    private final MainApp mainApp;
    private final DashboardView dashboardView;
    private UtilisateurDAO utilisateurDAO;

    public LoginView(MainApp mainApp, DashboardView dashboardView) {
        this.mainApp = mainApp;
        this.dashboardView = dashboardView;

        try {
            Connection conn = DatabaseConnection.getConnection();
            utilisateurDAO = new UtilisateurDAO(conn);
            utilisateurDAO.creerTable(); // assure que la table existe
        } catch (Exception e) {
            AlertUtils.showError("Erreur BDD : " + e.getMessage());
        }

        // Configuration des boutons radio
        radioInscription.setToggleGroup(choixGroup);
        radioConnexion.setToggleGroup(choixGroup);
        radioConnexion.setSelected(true); // par défaut, afficher connexion

        // Style
        this.setSpacing(15);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-padding: 20;");

        // --- Panneau Inscription ---
        nomField.setPromptText("Nom");
        prenomField.setPromptText("Prénom");
        emailFieldInscription.setPromptText("Email");
        mdpFieldInscription.setPromptText("Mot de passe");
        panelInscription.getChildren().addAll(
                new Label("Inscription"), nomField, prenomField,
                emailFieldInscription, mdpFieldInscription, btnInscrire
        );
        panelInscription.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");

        // --- Panneau Connexion ---
        emailFieldConnexion.setPromptText("Email");
        mdpFieldConnexion.setPromptText("Mot de passe");
        panelConnexion.getChildren().addAll(
                new Label("Connexion"), emailFieldConnexion,
                mdpFieldConnexion, btnConnexion
        );
        panelConnexion.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");

        // Actions des boutons
        btnInscrire.setOnAction(e -> inscrire());
        btnConnexion.setOnAction(e -> connexion());

        // Écouteur pour changer l'affichage dynamique
        choixGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == radioInscription) {
                this.getChildren().setAll(radioInscription, radioConnexion, panelInscription);
            } else {
                this.getChildren().setAll(radioInscription, radioConnexion, panelConnexion);
            }
        });

        // Affichage initial (connexion)
        this.getChildren().addAll(radioInscription, radioConnexion, panelConnexion);
    }

    private void inscrire() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailFieldInscription.getText().trim();
        String mdp = mdpFieldInscription.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
            AlertUtils.showError("Tous les champs sont obligatoires !");
            return;
        }

        try {
            Utilisateur u = new Utilisateur(0, nom, prenom, email, mdp);
            if (utilisateurDAO.ajouter(u)) {
                AlertUtils.showInfo("Inscription réussie !");
                // Vider les champs
                nomField.clear();
                prenomField.clear();
                emailFieldInscription.clear();
                mdpFieldInscription.clear();
                // Basculer automatiquement sur le panneau Connexion
                radioConnexion.setSelected(true);
            } else {
                AlertUtils.showError("Email déjà utilisé !");
            }
        } catch (Exception e) {
            AlertUtils.showError("Erreur inscription : " + e.getMessage());
        }
    }

    private void connexion() {
        String email = emailFieldConnexion.getText().trim();
        String mdp = mdpFieldConnexion.getText();

        if (email.isEmpty() || mdp.isEmpty()) {
            AlertUtils.showError("Email et mot de passe requis !");
            return;
        }

        try {
            Utilisateur u = utilisateurDAO.getByEmail(email);
            if (u != null && u.verifierMotDePasse(mdp)) {
                AlertUtils.showInfo("Connexion réussie !");
                dashboardView.setUtilisateurConnecte(u);
                dashboardView.chargerTransactions();
                mainApp.showDashboard();
            } else {
                AlertUtils.showError("Email ou mot de passe incorrect !");
            }
        } catch (Exception e) {
            AlertUtils.showError("Erreur connexion : " + e.getMessage());
        }
    }
}