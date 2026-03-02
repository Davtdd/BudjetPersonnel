package model;

public class Transaction {

    private int id;
    private String categorie;
    private String type;
    private double montant;
    private String date;


//    Constructeur

    public Transaction(){

    }

    public Transaction(int id, String categorie,String type,double montant,String date){

        this.id = id;
        this.categorie = categorie;
        this.type = type;
        this.montant = montant;
        this.date=date;

    }

    public int getId() {
        return id;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getType() {
        return type;
    }

    public double getMontant() {
        return montant;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", categorie='" + categorie + '\'' +
                ", type='" + type + '\'' +
                ", montant=" + montant +
                ", date='" + date + '\'' +
                '}';
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
