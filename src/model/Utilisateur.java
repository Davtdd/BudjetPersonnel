package model;

public class Utilisateur {

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasseHash;

    //Constructeur , pour construire utilisateur .
    public Utilisateur(int id,String nom,String prenom, String email, String motDePasse){
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        setMotDePasse(motDePasse);

    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasseHash = hash(motDePasse);
    }

    public boolean veifierMotDePasse(String motEntre){
        return hash(motEntre).equals(this.motDePasseHash);
    }

    private String hash(String input) {
        return "HASH_" + input; // simulation simple
    }

    public void setEmail(String email){
        if (email != null && email.contains("@")){
            this.email = email;
        }
    }
//    public boolean verifierMail(String email){
    ////
//    }

    //    Getters pour pouvoir voir les info des attribut de l'objet utilisateur construis.
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasseHash() {
        return motDePasseHash;
    }



    // toString() pour pouvoir afficher proprement les differentes reference
    @Override
    public  String toString(){
        return "Utilisateur {" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
