package service;

import model.Transaction;
import model.TypeTransaction;

import java.util.ArrayList;

public class TransactionService {

    private ArrayList<Transaction> transactions = new ArrayList<>();

    public void ajouterTransaction(Transaction t) {
        transactions.add(t);
    }

    public void afficherTransactions() {
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

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

    /**
     * Retourne une liste des transactions correspondant à une catégorie donnée.
     */
    public ArrayList<Transaction> filtrerParCategorie(String categorie) {

        ArrayList<Transaction> resultat = new ArrayList<>();

        for (Transaction t : transactions) {

            // On compare la catégorie
            if (t.getCategorie().equalsIgnoreCase(categorie)) {
                resultat.add(t);
            }
        }

        return resultat;
    }


    /**
     * Calcule uniquement le total des dépenses d'une catégorie (valeur positive).
     */
    public double calculerTotalDepensesParCategorie(String categorie) {

        double total = 0;

        for (Transaction t : transactions) {

            if (t.getCategorie().equalsIgnoreCase(categorie)
                    && t.getType() == TypeTransaction.DEPENSE) {

                total += t.getMontant();
            }
        }

        return total;
    }


    /**
     * Supprime une transaction selon son id.
     * Retourne true si suppression réussie, sinon false.
     */
    public boolean supprimerTransaction(int id) {

        for (int i = 0; i < transactions.size(); i++) {

            if (transactions.get(i).getId() == id) {
                transactions.remove(i);
                return true; // suppression réussie
            }
        }

        return false; // aucune transaction trouvée
    }


}