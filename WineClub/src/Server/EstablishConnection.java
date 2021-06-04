
/**
 * Name: Denys Khomenko     Sid:8097325
 */

package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * class  used to encapsulate the connection to the SQLite database.
 */
public class EstablishConnection {

    public static final String DB_URL = "jdbc:sqlite:WineClub\\src\\Server\\WineClubDatabase\\wine-data-small.sqlite";

    public static final String DB_USERNAME = "";
    public static final String DB_PASSWORD = "";

    /**
     * Attempt to connect to the given database URL and return a Connection object
     */
    public static Connection getConnection() throws SQLException {
        Connection connectionObj = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); //Attempt to connect to the given database URL
        System.out.println("Connected to SQLite");
        return connectionObj;
    }

}