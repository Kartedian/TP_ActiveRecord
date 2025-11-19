package activeRecord;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe DBConnection qui crée une connexion à notre système de gestion de base de données
 */
public class DBConnection {

    //nom d'utilisateur pour accéder
    private static String userName;
    //mot de passe pour accéder
    private static String password;
    //nom ou IP pour atteindre notre système de BD
    private static String serverName;
    //le port sur lequel il est hébergé
    private static String portNumber;
    //instance
    private static DBConnection instance;

    //nom de la BD à laquelle on veut accéder
    private String nameDB;
    //url avec laquelle nous accéderons au système
    private String urlDB;
    //objet que nous utiliserons pour atteindre la BD et communiquer avec elle
    private Connection connect;

    /**
     * Constructeur de la classe
     */
    private DBConnection() {
        this.urlDB = "jdbc:mysql://"+ serverName+":"+portNumber+"/";
    }

    /**
     * Méthode qui crée une instance si elle n'existe pas (Singleton).
     *
     * @return
     */
    public static DBConnection getInstance(){
        if(instance == null){
            instance = new DBConnection();
        }
        return instance;
    }

    /**
     * Méthode qui utilise un fichier, passé en paramètre, pour extraire les données d'accès au système de gestion
     * de BD.
     *
     * @param file
     */
    public static void setConfig(String file){
        Properties prop = new Properties();
        try(FileInputStream fis = new FileInputStream(file)){
            prop.load(fis);

            DBConnection.userName = prop.getProperty("user");
            DBConnection.password = prop.getProperty("password");
            DBConnection.serverName = prop.getProperty("serverName");
            DBConnection.portNumber = prop.getProperty("portNumber");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Méthode qui crée la connexion avec notre BD si elle n'existe pas, sinon renvoie celle déjà existante.
     *
     * @return
     */
    public Connection getConnect(){
        if(connect == null){
            try{
                Properties connectionProps = new Properties();
                connectionProps.put("user", userName);
                connectionProps.put("password", password);
                if(nameDB == null){
                    throw new Exception("DB name not defined.");
                }
                connect = DriverManager.getConnection((urlDB+nameDB), connectionProps);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        return this.connect;
    }

    /**
     * Méthode set pour le nom de la BD à laquelle on voudrait accéder.
     *
     * @param dbName
     */
    public void setBDName(String dbName){
        nameDB=dbName;
        connect=null;
    }

    /**
     * Méthode get pour retourne le nom de la BD.
     *
     * @return
     */
    public String getNameBD(){
        return nameDB;
    }
}