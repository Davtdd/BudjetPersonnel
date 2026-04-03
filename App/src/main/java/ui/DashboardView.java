package ui;

import dao.TransactionDAO;
import dao.UtilisateurDAO;
import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Transaction;
import model.TypeTransaction;
import model.Utilisateur;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DashboardView extends VBox {
    private final MainApp mainApp;
    private Utilisateur utilisateurConnecte;
    private TransactionDAO transactionDAO;

    // Composants UI
    private final Label soldeLabel = new Label("Solde : 0");
    private final TextField montantField = new TextField();
    private final ComboBox<String> categorieBox = new ComboBox<>();
    private final ListView<Transaction> listeTransactions = new ListView<>();
    private final PieChart pieChart = new PieChart();
    private final Button btnDepense = new Button("Ajouter Dépense");
    private final Button btnRevenu = new Button("Ajouter Revenu");
    private final Button btnDeconnexion = new Button("Déconnexion");

    // Nouveaux boutons
    private final Button btnModifier = new Button("Modifier");
    private final Button btnSupprimer = new Button("Supprimer");

    public DashboardView(MainApp mainApp) {
        this.mainApp = mainApp;
        try {
            Connection conn = DatabaseConnection.getConnection();
            transactionDAO = new TransactionDAO(conn);
            new UtilisateurDAO(conn).creerTable();
            transactionDAO.creerTable();
        } catch (Exception e) {
            AlertUtils.showError("Erreur BDD : " + e.getMessage());
        }

        montantField.setPromptText("Montant");
        categorieBox.getItems().addAll("Courses", "Transport", "Divertissement", "Logement", "Autres", "Salaire");
        categorieBox.getSelectionModel().select("Salaire");

        btnDepense.setOnAction(e -> ajouterTransaction(TypeTransaction.DEPENSE));
        btnRevenu.setOnAction(e -> ajouterTransaction(TypeTransaction.REVENU));
        btnDeconnexion.setOnAction(e -> deconnexion());

        // Actions des nouveaux boutons
        btnModifier.setOnAction(e -> modifierTransaction());
        btnSupprimer.setOnAction(e -> supprimerTransaction());

        // Disposition horizontale pour les boutons de gestion
        HBox gestionBox = new HBox(10, btnModifier, btnSupprimer);
        gestionBox.setPadding(new Insets(5, 0, 5, 0));

        activerTransaction(false);
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.getChildren().addAll(
                btnDeconnexion, soldeLabel, montantField, categorieBox,
                btnDepense, btnRevenu, listeTransactions, gestionBox, pieChart
        );
    }

    public void setUtilisateurConnecte(Utilisateur u) {
        this.utilisateurConnecte = u;
        activerTransaction(true);
        mettreAJourSolde();
    }

    public void chargerTransactions() {
        if (utilisateurConnecte == null) return;
        try {
            List<Transaction> txs = transactionDAO.getTransactionsByUtilisateur(utilisateurConnecte.getId());
            listeTransactions.getItems().clear();
            listeTransactions.getItems().addAll(txs);
            mettreAJourPieChart();
            mettreAJourSolde();
        } catch (Exception e) {
            AlertUtils.showError("Erreur chargement : " + e.getMessage());
        }
    }

    private void ajouterTransaction(TypeTransaction type) {
        if (utilisateurConnecte == null) {
            AlertUtils.showError("Connectez-vous !");
            return;
        }
        try {
            double montant = Double.parseDouble(montantField.getText());
            String cat = categorieBox.getValue();
            String date = LocalDate.now().toString();
            Transaction t = new Transaction(0, type, montant, cat, date);
            transactionDAO.ajouter(t, utilisateurConnecte.getId());
            listeTransactions.getItems().add(t);
            mettreAJourPieChart();
            mettreAJourSolde();
            montantField.clear();
        } catch (Exception e) {
            AlertUtils.showError("Erreur transaction : " + e.getMessage());
        }
    }

    // Nouvelle méthode : modifier une transaction sélectionnée
    private void modifierTransaction() {
        Transaction selectionnee = listeTransactions.getSelectionModel().getSelectedItem();
        if (selectionnee == null) {
            AlertUtils.showError("Veuillez sélectionner une transaction à modifier.");
            return;
        }

        // Créer une boîte de dialogue personnalisée
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.setTitle("Modifier la transaction");
        dialog.setHeaderText("Modifier les informations");

        // Champs de saisie pré-remplis
        TextField montantField = new TextField(String.valueOf(selectionnee.getMontant()));
        ComboBox<String> categorieBox = new ComboBox<>();
        categorieBox.getItems().addAll("Courses", "Transport", "Divertissement", "Logement", "Autres", "Salaire");
        categorieBox.setValue(selectionnee.getCategorie());

        ComboBox<TypeTransaction> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(TypeTransaction.values());
        typeBox.setValue(selectionnee.getType());

        DatePicker datePicker = new DatePicker(LocalDate.parse(selectionnee.getDate()));

        VBox content = new VBox(10,
                new Label("Montant :"), montantField,
                new Label("Catégorie :"), categorieBox,
                new Label("Type :"), typeBox,
                new Label("Date :"), datePicker
        );
        dialog.getDialogPane().setContent(content);

        ButtonType btnValider = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnValider, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == btnValider) {
                try {
                    double montant = Double.parseDouble(montantField.getText());
                    String categorie = categorieBox.getValue();
                    TypeTransaction type = typeBox.getValue();
                    String date = datePicker.getValue().toString();
                    // Créer une nouvelle transaction avec l'ancien ID
                    Transaction t = new Transaction(selectionnee.getId(), type, montant, categorie, date);
                    return t;
                } catch (NumberFormatException e) {
                    AlertUtils.showError("Montant invalide.");
                    return null;
                }
            }
            return null;
        });

        Optional<Transaction> result = dialog.showAndWait();
        result.ifPresent(t -> {
            try {
                transactionDAO.modifier(t);
                // Mettre à jour la liste
                int index = listeTransactions.getSelectionModel().getSelectedIndex();
                listeTransactions.getItems().set(index, t);
                mettreAJourPieChart();
                mettreAJourSolde();
                AlertUtils.showInfo("Transaction modifiée avec succès.");
            } catch (Exception e) {
                AlertUtils.showError("Erreur lors de la modification : " + e.getMessage());
            }
        });
    }

    // Nouvelle méthode : supprimer une transaction sélectionnée
    private void supprimerTransaction() {
        Transaction selectionnee = listeTransactions.getSelectionModel().getSelectedItem();
        if (selectionnee == null) {
            AlertUtils.showError("Veuillez sélectionner une transaction à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Supprimer la transaction de " + selectionnee.getMontant() + "€ ?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> reponse = confirmation.showAndWait();
        if (reponse.isPresent() && reponse.get() == ButtonType.YES) {
            try {
                transactionDAO.supprimer(selectionnee.getId());
                listeTransactions.getItems().remove(selectionnee);
                mettreAJourPieChart();
                mettreAJourSolde();
                AlertUtils.showInfo("Transaction supprimée.");
            } catch (Exception e) {
                AlertUtils.showError("Erreur lors de la suppression : " + e.getMessage());
            }
        }
    }

    private void mettreAJourPieChart() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        for (Transaction t : listeTransactions.getItems()) {
            if (t.getType() == TypeTransaction.DEPENSE) {
                data.add(new PieChart.Data(t.getCategorie() + " (" + t.getMontant() + "€)", t.getMontant()));
            }
        }
        pieChart.setData(data);
    }

    private void mettreAJourSolde() {
        double solde = 0;
        for (Transaction t : listeTransactions.getItems()) {
            if (t.getType() == TypeTransaction.REVENU) solde += t.getMontant();
            else solde -= t.getMontant();
        }
        soldeLabel.setText(String.format("Solde : %.2f €", solde));
    }

    private void activerTransaction(boolean actif) {
        montantField.setDisable(!actif);
        categorieBox.setDisable(!actif);
        btnDepense.setDisable(!actif);
        btnRevenu.setDisable(!actif);
        btnModifier.setDisable(!actif);
        btnSupprimer.setDisable(!actif);
    }

    private void deconnexion() {
        utilisateurConnecte = null;
        activerTransaction(false);
        listeTransactions.getItems().clear();
        pieChart.getData().clear();
        soldeLabel.setText("Solde : 0");
        AlertUtils.showInfo("Déconnecté !");
        mainApp.showLogin();
    }
}