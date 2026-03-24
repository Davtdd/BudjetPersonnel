package ui;

import model.Transaction;
import model.TypeTransaction;
import service.GestionBudget;

public class Main {
    public static void main(String[] args) {

        GestionBudget gestion = new GestionBudget();

        Transaction t1 = new Transaction(1, TypeTransaction.REVENU, 2000, "Salaire","01/01/2026");
        Transaction t2 = new Transaction(2, TypeTransaction.DEPENSE, 500, "Courses","02/02/2026");

        gestion.ajouterTransaction(t1);
        gestion.ajouterTransaction(t2);

        gestion.afficherTransactions();

        System.out.println("Solde : " + gestion.calculerSolde());
    }
}