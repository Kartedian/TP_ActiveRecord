package activeRecord;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static java.util.Optional.empty;

public class DBCConnection {

    private static String userName;
    private static String password;
    private static String serverName;
    private static String portNumber;
    private static DBCConnection instance;

    private String nameDB;
    private String urlDB;
    private Connection connect;

    private DBCConnection() {
        this.urlDB = "jdbc:mysql://"+ serverName+":"+portNumber+"/";
    }

    public static DBCConnection getInstance(){
        if(instance == null){
            instance = new DBCConnection();
        }
        return instance;
    }

    public static void setConfig(String file){
        Properties prop = new Properties();
        try(FileInputStream fis = new FileInputStream(file)){
            prop.load(fis);

            DBCConnection.userName = prop.getProperty("user");
            DBCConnection.password = prop.getProperty("password");
            DBCConnection.serverName = prop.getProperty("serverName");
            DBCConnection.portNumber = prop.getProperty("portNumber");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

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

    public void setDbName(String dbName){
        nameDB=dbName;
        connect=null;
    }
}
