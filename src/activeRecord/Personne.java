package activeRecord;

import java.sql.*;
import java.util.ArrayList;

public class Personne {

    private int id;

    private String nom;

    private String prenom;

    private static final DBConnection dbConnection = DBConnection.getInstance();

    public  Personne(String nom, String prenom) {
        this.id = -1;
        this.nom = nom;
        this.prenom = prenom;
    }

    private Personne(int id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }

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

    public static Personne findById(int id) throws Exception {
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

    public static ArrayList<Personne> findByName(String nom) throws Exception {
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

    public void save(){
        if (this.id == -1) {
            this.saveNew();
        }
        else{
            this.update();
        }
    }

    private void saveNew(){
        try {
            Connection connection = dbConnection.getConnect();
            PreparedStatement sql;

            String query = "INSERT INTO Personne (nom, prenom) VALUES (?,?);";

            sql = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            sql.setString(1, this.nom);
            sql.setString(2, this.prenom);

            sql.execute();

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
