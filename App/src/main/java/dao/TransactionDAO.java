package dao;

import model.Transaction;
import model.TypeTransaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    private Connection conn;

    public TransactionDAO(Connection conn) {
        this.conn = conn;
    }

    public void creerTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS transaction (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "utilisateur_id INT," +
                "type VARCHAR(20)," +
                "montant DOUBLE," +
                "categorie VARCHAR(50)," +
                "date VARCHAR(20)," +
                "FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id)" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    // Ajouter transaction
    public void ajouter(Transaction t, int utilisateurId) throws SQLException {
        String sql = "INSERT INTO transaction (utilisateur_id, type, montant, categorie, date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, utilisateurId);
            ps.setString(2, t.getType().toString());
            ps.setDouble(3, t.getMontant());
            ps.setString(4, t.getCategorie());
            ps.setString(5, t.getDate());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) t.setId(rs.getInt(1));
            }
        }
    }

    // Supprimer transaction par ID
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM transaction WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Lister transactions d’un utilisateur
    public List<Transaction> getTransactionsByUtilisateur(int utilisateurId) throws SQLException {
        List<Transaction> liste = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE utilisateur_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(new Transaction(
                            rs.getInt("id"),
                            TypeTransaction.valueOf(rs.getString("type")),
                            rs.getDouble("montant"),
                            rs.getString("categorie"),
                            rs.getString("date")
                    ));
                }
            }
        }
        return liste;
    }

    //Modifier une transaction existante
    public void modifier(Transaction t) throws SQLException {
        String sql = "UPDATE transaction SET type = ?, montant = ?, categorie = ?, date = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getType().toString());
            ps.setDouble(2, t.getMontant());
            ps.setString(3, t.getCategorie());
            ps.setString(4, t.getDate());
            ps.setInt(5, t.getId());
            ps.executeUpdate();
        }
    }
}