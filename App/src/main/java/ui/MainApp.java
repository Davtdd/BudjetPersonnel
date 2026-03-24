package ui;

import dao.TransactionDAO;
import dao.UtilisateurDAO;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Transaction;
import model.TypeTransaction;
import model.Utilisateur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.List;

public class MainApp extends Application {

    private Connection conn;
    private UtilisateurDAO utilisateurDAO;
    private TransactionDAO transactionDAO;

    private Utilisateur utilisateurConnecte;

    // Composants JavaFX
    private Label soldeLabel = new Label("Solde : 0");
    private TextField montantField = new TextField();
    private ComboBox<String> categorieBox = new ComboBox<>();
    private ListView<Transaction> listeTransactions = new ListView<>();
    private PieChart pieChart = new PieChart();
    private TextField emailField = new TextField();
    private PasswordField mdpField = new PasswordField();
    private Button btnInscrire = new Button("S'inscrire");
    private Button btnConnexion = new Button("Se connecter");
    private Button btnDeconnexion = new Button("Déconnexion");
    private Button btnDepense = new Button("Ajouter Dépense");
    private Button btnRevenu = new Button("Ajouter Revenu");

    @Override
    public void start(Stage stage) throws Exception {
        // --- Connexion MySQL ---
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_budget", "root", "");
        utilisateurDAO = new UtilisateurDAO(conn);
        transactionDAO = new TransactionDAO(conn);
        utilisateurDAO.creerTable();
        transactionDAO.creerTable();

        // --- UI ---
        emailField.setPromptText("Email");
        mdpField.setPromptText("Mot de passe");
        montantField.setPromptText("Montant");
        categorieBox.getItems().addAll("Courses","Transport","Divertissement","Logement","Autres","Salaire");
        categorieBox.getSelectionModel().select("Salaire");
        btnDepense.setDisable(true);

        btnConnexion.setOnAction(e -> connexion());
        btnInscrire.setOnAction(e -> inscrire());
        btnDeconnexion.setOnAction(e -> deconnexion());
        btnDepense.setOnAction(e -> ajouterTransaction(TypeTransaction.DEPENSE));
        btnRevenu.setOnAction(e -> ajouterTransaction(TypeTransaction.REVENU));

        VBox root = new VBox(10);
        root.getChildren().addAll(emailField, mdpField, btnInscrire, btnConnexion, btnDeconnexion,
                soldeLabel, montantField, categorieBox, btnDepense, btnRevenu,
                listeTransactions, pieChart);

        activerTransaction(false);

        Scene scene = new Scene(root, 550, 600);
        stage.setScene(scene);
        stage.setTitle("Gestion Budget avec MySQL");
        stage.show();
    }

    private void inscrire() {
        try {
            Utilisateur u = new Utilisateur(0, "Nom", "Prenom", emailField.getText(), mdpField.getText());
            if(utilisateurDAO.ajouter(u)) {
                afficherInfo("Inscription réussie !");
            } else afficherErreur("Email déjà utilisé !");
        } catch (Exception e) {
            afficherErreur("Erreur inscription : " + e.getMessage());
        }
    }

    private void connexion() {
        try {
            Utilisateur u = utilisateurDAO.getByEmail(emailField.getText());
            if(u != null && u.verifierMotDePasse(mdpField.getText())) {
                utilisateurConnecte = u;
                afficherInfo("Connexion réussie !");
                activerTransaction(true);
                chargerTransactions();
            } else afficherErreur("Email ou mot de passe incorrect !");
        } catch(Exception e) {
            afficherErreur("Erreur connexion : " + e.getMessage());
        }
    }

    private void deconnexion() {
        utilisateurConnecte = null;
        activerTransaction(false);
        listeTransactions.getItems().clear();
        pieChart.getData().clear();
        soldeLabel.setText("Solde : 0");
        afficherInfo("Déconnecté !");
    }

    private void ajouterTransaction(TypeTransaction type) {
        if(utilisateurConnecte == null) {
            afficherErreur("Connectez-vous !");
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
            montantField.clear();
        } catch(Exception e) {
            afficherErreur("Erreur transaction : " + e.getMessage());
        }
    }

    private void chargerTransactions() {
        try {
            List<Transaction> txs = transactionDAO.getTransactionsByUtilisateur(utilisateurConnecte.getId());
            listeTransactions.getItems().clear();
            listeTransactions.getItems().addAll(txs);
            mettreAJourPieChart();
        } catch(Exception e) {
            afficherErreur("Erreur chargement : " + e.getMessage());
        }
    }

    private void mettreAJourPieChart() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        for(Transaction t : listeTransactions.getItems()) {
            if(t.getType() == TypeTransaction.DEPENSE) {
                data.add(new PieChart.Data(t.getCategorie() + " (" + t.getMontant() + "€)", t.getMontant()));
            }
        }
        pieChart.setData(data);
    }

    private void activerTransaction(boolean actif) {
        montantField.setDisable(!actif);
        categorieBox.setDisable(!actif);
        btnDepense.setDisable(!actif);
        btnRevenu.setDisable(!actif);
    }

    private void afficherErreur(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg); a.showAndWait();
    }
    private void afficherInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg); a.showAndWait();
    }

    public static void main(String[] args) { launch(); }
}