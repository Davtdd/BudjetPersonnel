package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utilisateur {

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasseHash;

    public Utilisateur(int id, String nom, String prenom, String email, String motDePasse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        setMotDePasse(motDePasse); // Hash automatiquement
    }

    // Hash le mot de passe et stocke
    public void setMotDePasse(String motDePasse) {
        this.motDePasseHash = hash(motDePasse);
    }

    // Vérifie un mot de passe en comparant les hash
    public boolean verifierMotDePasse(String motEntre) {
        return hash(motEntre).equals(this.motDePasseHash);
    }

    // Génération SHA-256
    private String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convertir les bytes en hexadécimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // en cas d’erreur
        }
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }


    // setter package-private pour DAO seulement
    public void setId(int id) {
        this.id = id;
    }
    // getter package-private pour DAO seulement
    public String getMotDePasseHashForDAO() {
        return motDePasseHash;
    }
    /**
     * toString pour afficher proprement l'utilisateur
     */

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}



