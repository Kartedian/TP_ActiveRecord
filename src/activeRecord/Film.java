package activeRecord;

public class Film {

    private String titre;
    private int id;
    private int id_real;

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
                    " ID_REA int(11) DEFAULT NULL" +
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
}
