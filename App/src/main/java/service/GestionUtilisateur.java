package service;

import model.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class GestionUtilisateur {

    private List<Utilisateur> utilisateurs;
    private Utilisateur utilisateurConnecte;

    public GestionUtilisateur() {
        utilisateurs = new ArrayList<>();
        utilisateurConnecte = null;
    }

    // Inscription
    public boolean inscrireUtilisateur(Utilisateur u) {
        // Vérifie si email déjà utilisé
        for (Utilisateur user : utilisateurs) {
            if (user.getEmail().equalsIgnoreCase(u.getEmail())) {
                return false; // email déjà pris
            }
        }
        utilisateurs.add(u);
        return true;
    }

    // Connexion
    public boolean connecter(String email, String motDePasse) {
        for (Utilisateur u : utilisateurs) {
            if (u.getEmail().equalsIgnoreCase(email) && u.verifierMotDePasse(motDePasse)) {
                utilisateurConnecte = u;
                return true;
            }
        }
        return false;
    }

    // Déconnexion
    public void deconnecter() {
        utilisateurConnecte = null;
    }

    // Getter utilisateur courant
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    // Liste utilisateurs (utile pour test)
    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }
}