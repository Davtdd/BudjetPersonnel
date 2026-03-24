package dao;

import model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    private Connection conn;

    public UtilisateurDAO(Connection conn) {
        this.conn = conn;
    }

    // Créer la table si elle n'existe pas
    public void creerTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS utilisateur (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nom VARCHAR(50)," +
                "prenom VARCHAR(50)," +
                "email VARCHAR(100) UNIQUE," +
                "motDePasseHash VARCHAR(255)" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    // Inscrire un utilisateur
    public boolean ajouter(Utilisateur u) throws SQLException {
        String sql = "INSERT INTO utilisateur (nom, prenom, email, motDePasseHash) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getMotDePasseHashForDAO()); // hash déjà généré dans l'objet
            int affected = ps.executeUpdate();
            if (affected == 0) return false;

            // récupérer l'ID généré et le mettre dans l'objet
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setId(rs.getInt(1)); // ici setter pour ID est utile
                }
            }
            return true;
        }
    }

    // Récupérer un utilisateur par email
    public Utilisateur getByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Utilisateur(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("motDePasseHash") // récupère le hash
                    );
                }
            }
        }
        return null;
    }

    // Récupérer tous les utilisateurs
    public List<Utilisateur> getAll() throws SQLException {
        List<Utilisateur> liste = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("motDePasseHash")
                ));
            }
        }
        return liste;
    }
}