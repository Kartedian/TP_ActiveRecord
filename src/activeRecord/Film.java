package activeRecord;

import java.sql.*;

public class Film {

    private String titre;
    private int id;
    private int id_real;

    private static final DBConnection dbConnection = DBConnection.getInstance();

    public Film(String titre, Personne personne){
        this.titre = titre;
        this.id_real = personne.getId();
        this.id = -1;
    }

    private Film(String titre, int id, int id_real){
        this.titre = titre;
        this.id = id;
        this.id_real = id_real;
    }

    public String getTitre(){
        return this.titre;
    }

    public int getId(){
        return this.id;
    }

    public int getId_real(){
        return this.id_real;
    }

    public void setTitre(String titre){
        this.titre = titre;
    }

    public static Film findById(int id){
        try{
            Connection connection = dbConnection.getConnect();

            String query = "SELECT * FROM Film WHERE id = ?;";

            PreparedStatement sql = connection.prepareStatement(query);
            sql.setInt(1, id);
            sql.execute();

            ResultSet rs = sql.getResultSet();

            if(rs.next()){
                return new Film(rs.getString("titre"), rs.getInt("id"), rs.getInt("id_rea"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Personne getRealisateur(){
        try {
            return Personne.findById(this.id_real);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTable(){
        Connection connection = dbConnection.getConnect();
        try{
            String createString = "CREATE TABLE Film (" +
                    " ID INTEGER AUTO_INCREMENT, "+
                    " TITRE varchar(40) NOT NULL, " +
                    " ID_REA int(11) NOT NULL, " +
                    " PRIMARY KEY (ID), " +
                    " FOREIGN KEY (ID_REA) REFERENCES Personne(ID))";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(createString);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteTable(){
        Connection connection = dbConnection.getConnect();
        try{
            String createString = "DROP TABLE Film";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(createString);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(){
        try {
            if (this.id_real != -1)
                if (this.id == -1) {
                    this.saveNew();
                } else {
                    this.update();
                }
            else
                throw new RealisateurAbsentException();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveNew(){
        try {
            Connection connection = dbConnection.getConnect();
            PreparedStatement sql;

            String query = "INSERT INTO Film (titre, id_rea) VALUES (?,?);";

            sql = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            sql.setString(1, this.titre);
            sql.setInt(2, this.id_real);

            sql.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(){
        try {
            Connection connection = dbConnection.getConnect();
            PreparedStatement sql;

            String query = "update Film set titre = ?, id_rea = ? where id = ? ;";

            sql = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            sql.setString(1, this.titre);
            sql.setInt(2, this.id_real);
            sql.setInt(3, this.id);

            sql.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(){
        if(this.id!=(-1)){
            try{
                Connection connection = dbConnection.getConnect();

                String query = "DELETE FROM Film where titre = ? and id_rea = ?";
                PreparedStatement sql = connection.prepareStatement(query);
                sql.setString(1, this.titre);
                sql.setInt(2, this.id_real);
                sql.execute();
                this.id=-1;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
