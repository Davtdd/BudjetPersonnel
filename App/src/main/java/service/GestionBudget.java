package service;

import model.Transaction;
import model.TypeTransaction;

import java.util.ArrayList;
import java.util.List;

public class GestionBudget {

    private List<Transaction> transactions;

    // Constructeur
    public GestionBudget() {
        transactions = new ArrayList<>();
    }

    // Ajouter une transaction
    public void ajouterTransaction(Transaction t) {
        transactions.add(t);
    }

    // Supprimer une transaction
    public void supprimerTransaction(Transaction t) {
        transactions.remove(t);
    }

    // Calculer le solde
    public double calculerSolde() {
        double solde = 0;

        for (Transaction t : transactions) {
            if (t.getType() == TypeTransaction.REVENU) {
                solde += t.getMontant();
            } else {
                solde -= t.getMontant();
            }
        }

        return solde;
    }

    // Afficher toutes les transactions (optionnel)
    public void afficherTransactions() {
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    // **Getter nécessaire pour MainApp et le PieChart**
    public List<Transaction> getTransactions() {
        return transactions;
    }
}