package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import database.DatabaseConnectionException;

/**
 * Si occupa della gestione della comunicazione tra il server ed il database
 */
public class DbAccess {
    private final String DBMS = "jdbc:mysql";
    private final String SERVER = "localhost";
    private final String DATABASE = "MapDB";
    private final int PORT = 3306;
    private final String USER_ID = "MapUser";
    private final String PASSWORD = "map";
    private String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
    private Connection conn;
    private Statement stat;

    public void initConnection() throws DatabaseConnectionException {
        try {
            //Class.forName(DRIVER_CLASS_NAME);
            String url = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE;
            conn = DriverManager.getConnection(url, USER_ID, PASSWORD);
            stat = conn.createStatement();
        } /*catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/ catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionException(e.getMessage());
        }

    }

    public Connection getConnection() 
    {
        return conn;
    }
    
    public Statement getStatement ()
    {
    	return stat;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
