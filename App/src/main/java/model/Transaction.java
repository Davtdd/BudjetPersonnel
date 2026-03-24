package model;

public class Transaction {
    private int id; // retirer final
    private TypeTransaction type;
    private double montant;
    private String categorie;
    private String date;

    public Transaction(int id, TypeTransaction type, double montant, String categorie, String date) {
        this.id = id;
        this.type = type;
        this.montant = montant;
        this.categorie = categorie;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; } // <- nouveau setter
    public TypeTransaction getType() { return type; }
    public double getMontant() { return montant; }
    public String getCategorie() { return categorie; }
    public String getDate() { return date; }

    @Override
    public String toString() {
        return id + " | " + type + " | " + montant + "€ | " + categorie + " | " + date;
    }
}