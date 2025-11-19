package activeRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public static ArrayList<Personne> findAll() throws Exception {
        if(Personne.dbConnection.getNameDB().equals("testpersonne")){
            Connection connection =  dbConnection.getConnect();
            ArrayList<Personne> personnes = new ArrayList<Personne>();

            String query = "SELECT * FROM personne;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            while(rs.next()){
                personnes.add(new Personne(rs.getInt("id"), rs.getString("nom"),rs.getString("prenom")));
            }
            return personnes;
        }
        throw new Exception("c'est pas la bon BD.");
    }

    public static Personne findById(int id) throws Exception {
        if(Personne.dbConnection.getNameDB().equals("testpersonne")){
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
        }
        throw new Exception("c'est pas la bon BD.");
    }

    public static ArrayList<Personne> findByName(String nom) throws Exception {
        if(Personne.dbConnection.getNameDB().equals("testpersonne")){
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
        }
        throw new Exception("c'est pas la bon BD.");
    }
}
