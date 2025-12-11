package activeRecord;

import java.sql.*;
import java.util.ArrayList;

/**
 * Classe représentant un tuple de la table Film selon le patron Active Record.
 * Un film est caractérisé par un titre et un réalisateur (clé étrangère).
 */
public class Film {

    private String titre;
    private int id;
    /**
     * Identifiant du réalisateur (clé étrangère vers la table Personne).
     */
    private int id_real;

    private static final DBConnection dbConnection = DBConnection.getInstance();

    /**
     * Constructeur public pour créer un Film à partir d'un titre et d'un objet Personne (réalisateur).
     * L'attribut id est initialisé à -1.
     *
     * @param titre    Le titre du film
     * @param personne L'objet Personne représentant le réalisateur (dont on extrait l'id)
     */
    public Film(String titre, Personne personne){
        this.titre = titre;
        this.id_real = personne.getId();
        this.id = -1;
    }

    /**
     * Constructeur privé pour reconstruire un objet Film à partir des résultats d'une requête SQL.
     *
     * @param titre   Le titre du film
     * @param id      L'identifiant du film
     * @param id_real L'identifiant du réalisateur
     */
    private Film(String titre, int id, int id_real){
        this.titre = titre;
        this.id = id;
        this.id_real = id_real;
    }

    // Getters et Setters

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

    /**
     * Retourne l'objet Film correspondant au tuple avec l'id passé en paramètre.
     *
     * @param id L'identifiant du film
     * @return L'objet Film trouvé ou null.
     */
    public static Film findById(int id){
        try{
            Connection connection = dbConnection.getConnect();

            String query = "SELECT * FROM Film WHERE id = ?;";

            PreparedStatement sql = connection.prepareStatement(query);
            sql.setInt(1, id);
            sql.execute();

            ResultSet rs = sql.getResultSet();

            if(rs.next()){
                // Note : il y a une potentielle erreur de typo "id_rea" vs "id_real" dans le code original, conservée ici.
                return new Film(rs.getString("titre"), rs.getInt("id"), rs.getInt("id_rea"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Retourne l'objet Personne correspondant au réalisateur du film.
     * Utilise l'Active Record Personne et l'attribut id_real.
     *
     * @return L'objet Personne réalisateur
     */
    public Personne getRealisateur(){
        try {
            return Personne.findById(this.id_real);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crée la table Film avec une clé étrangère référençant la table Personne.
     */
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

    /**
     * Supprime la table Film de la base de données.
     */
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

    /**
     * Sauvegarde le film dans la base.
     * Si la clé étrangère id_real vaut -1 (réalisateur non sauvegardé), lève une exception.
     * Sinon, effectue une insertion (si id=-1) ou une mise à jour.
     */
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

    /**
     * Insère un nouveau film dans la base.
     */
    private void saveNew(){
        try {
            Connection connection = dbConnection.getConnect();
            PreparedStatement sql;

            String query = "INSERT INTO Film (titre, id_rea) VALUES (?,?);";

            sql = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            sql.setString(1, this.titre);
            sql.setInt(2, this.id_real);

            sql.execute();
            // Note: Le code original ne met pas à jour this.id après l'insertion ici.
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Met à jour les informations du film existant.
     */
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

    /**
     * Supprime le film de la base et remet son id à -1.
     */
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

    /**
     * Retourne l'ensemble des films réalisés par la personne passée en paramètre.
     *
     * @param p La personne (réalisateur)
     * @return Une liste de films ou null.
     */
    public static ArrayList<Film> findByRealisateur(Personne p){
        try{
            Connection connection = dbConnection.getConnect();
            ArrayList<Film> films = new ArrayList<Film>();

            String requete = "SELECT * FROM Film WHERE id_rea = ?";

            PreparedStatement sql = connection.prepareStatement(requete);
            sql.setInt(1, p.getId());
            sql.execute();
            ResultSet rs = sql.getResultSet();

            while(rs.next()){
                films.add(new Film(rs.getString("titre"), rs.getInt("id"), rs.getInt("id_rea")));
            }
            if(films.isEmpty()){
                return null;
            }
            return films;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}