package activeRecord;

import java.sql.*;
import java.util.ArrayList;

/**
 * Classe représentant un tuple de la table personne selon le patron Active Record.
 * Elle permet de gérer les données d'une personne (nom, prénom) et d'interagir avec la base de données.
 */
public class Personne {

    /**
     * Identifiant unique de la personne dans la base de données.
     * Vaut -1 si la personne n'est pas encore enregistrée dans la base.
     */
    private int id;

    private String nom;

    private String prenom;

    // Instance unique de la connexion à la base de données (Singleton)
    private static final DBConnection dbConnection = DBConnection.getInstance();

    /**
     * Constructeur public pour créer une nouvelle Personne dans l'application.
     * L'identifiant est initialisé à -1 car la personne n'existe pas encore en base.
     *
     * @param nom    Le nom de la personne
     * @param prenom Le prénom de la personne
     */
    public  Personne(String nom, String prenom) {
        this.id = -1;
        this.nom = nom;
        this.prenom = prenom;
    }

    /**
     * Constructeur privé utilisé pour recréer un objet Personne à partir d'un tuple existant dans la base de données.
     *
     * @param id     L'identifiant récupéré de la base
     * @param nom    Le nom récupéré de la base
     * @param prenom Le prénom récupéré de la base
     */
    private Personne(int id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }

    // Getters et Setters standards

    public int getId(){
        return id;
    }

    public String getNom(){
        return this.nom;
    }

    public String getPrenom(){
        return this.prenom;
    }

    public void setNom(String nom){
        this.nom = nom;
    }

    public void setPrenom(String prenom){
        this.prenom = prenom;
    }

    /**
     * Récupère l'ensemble des tuples de la table personne sous forme d'objets.
     *
     * @return Une ArrayList contenant toutes les personnes, ou null si la table est vide.
     */
    public static ArrayList<Personne> findAll(){
        try{
            Connection connection =  dbConnection.getConnect();
            ArrayList<Personne> personnes = new ArrayList<Personne>();

            String query = "SELECT * FROM personne;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            while(rs.next()){
                personnes.add(new Personne(rs.getInt("id"), rs.getString("nom"),rs.getString("prenom")));
            }
            if(personnes.isEmpty())
                return null;
            else
                return personnes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recherche et retourne l'objet Personne correspondant au tuple ayant l'id passé en paramètre.
     *
     * @param id L'identifiant de la personne à rechercher
     * @return L'objet Personne correspondant, ou null si l'objet n'existe pas.
     */
    public static Personne findById(int id){
        try{
            Connection connection =  dbConnection.getConnect();

            String query = "SELECT * FROM personne WHERE id = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            if(rs.next()){
                return new Personne(rs.getInt("id"), rs.getString("nom"),rs.getString("prenom"));
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retourne la liste des objets Personne correspondant aux tuples dont le nom est passé en paramètre.
     *
     * @param nom Le nom à rechercher
     * @return Une liste de personnes portant ce nom, ou null si aucune correspondance.
     */
    public static ArrayList<Personne> findByName(String nom){
        try{
            Connection connection =  dbConnection.getConnect();
            ArrayList<Personne> personnes = new ArrayList<Personne>();

            String query = "SELECT * FROM personne where nom = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, nom);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            while(rs.next()){
                personnes.add(new Personne(rs.getInt("id"), rs.getString("nom"),rs.getString("prenom")));
            }
            if(personnes.isEmpty())
                return null;
            else
                return personnes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Crée la table Personne dans la base de données.
     */
    public static void createTable(){
        Connection connection = dbConnection.getConnect();
        try{
            String createString = "CREATE TABLE Personne ( "
                    + "ID INTEGER  AUTO_INCREMENT, " + "NOM varchar(40) NOT NULL, "
                    + "PRENOM varchar(40) NOT NULL, " + "PRIMARY KEY (ID))";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(createString);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Supprime la table Personne de la base de données.
     */
    public static void deleteTable(){
        Connection connection = dbConnection.getConnect();
        try{
            String createString = "DROP TABLE Personne";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(createString);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Supprime la personne actuelle de la base de données.
     * Après suppression, l'attribut id est remis à -1 car l'objet n'est plus présent dans la table.
     */
    public void delete(){
        if(this.id!=(-1)){
            try{
                Connection connection = dbConnection.getConnect();

                String query = "DELETE FROM Personne where nom = ? and prenom = ?";
                PreparedStatement sql = connection.prepareStatement(query);
                sql.setString(1, this.nom);
                sql.setString(2, this.prenom);
                sql.execute();
                this.id=-1;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sauvegarde l'objet Personne dans la table.
     * Si l'id vaut -1, cela effectue une insertion (nouvelle personne).
     * Si l'id est différent de -1, cela effectue une mise à jour du tuple existant.
     */
    public void save(){
        if (this.id == -1) {
            this.saveNew();
        }
        else{
            this.update();
        }
    }

    /**
     * Méthode privée pour insérer un nouveau tuple dans la base.
     * Met à jour l'attribut id avec la clé générée par l'auto-incrément.
     */
    private void saveNew(){
        try {
            Connection connection = dbConnection.getConnect();
            PreparedStatement sql;

            String query = "INSERT INTO Personne (nom, prenom) VALUES (?,?);";

            sql = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            sql.setString(1, this.nom);
            sql.setString(2, this.prenom);

            sql.execute();

            // Récupération de l'ID généré (bien que la requête suivante fasse un select explicite)
            query = "SELECT id FROM Personne where nom = ? and prenom = ?";
            sql = connection.prepareStatement(query);
            sql.setString(1, this.nom);
            sql.setString(2, this.prenom);
            sql.execute();
            ResultSet rs = sql.getResultSet();

            if(rs.next()){
                this.id = rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Méthode privée pour mettre à jour un tuple existant dans la base.
     */
    private void update(){
        try {
            Connection connection = dbConnection.getConnect();
            PreparedStatement sql;

            String query = "update Personne set nom = ?, prenom = ? where id = ? ;";

            sql = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            sql.setString(1, this.nom);
            sql.setString(2, this.prenom);
            sql.setInt(3, this.id);

            sql.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}