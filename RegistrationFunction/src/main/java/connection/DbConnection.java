package connection;
import utils.DatabaseSecret;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    // Create a static Connection object
    public static Connection connection;

    // Initialize the HikariDataSource and set up the database connection properties
    static {
        String url = "jdbc:"+
                DatabaseSecret.getDbData.getEngine()+
                "://"+ DatabaseSecret.getDbData.getHost()+
                ":"+DatabaseSecret.getDbData.getPort()+
                "/"+DatabaseSecret.getDbData.getDbname();
        try {
            connection = DriverManager.getConnection(url,
                    DatabaseSecret.getDbData.getUsername(),
                    DatabaseSecret.getDbData.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to establish connection: "+e.getMessage());
        }
    }

}
