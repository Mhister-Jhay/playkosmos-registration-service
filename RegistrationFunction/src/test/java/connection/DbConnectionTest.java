package connection;
import utils.TestReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionTest {
    // Create a static Connection object
    public static Connection connection;

    // Initialize the HikariDataSource and set up the database connection properties
    static {
        String url = "jdbc:"+
                TestReader.config.get("engine")+
                "://"+ TestReader.config.get("host")+
                ":"+TestReader.config.get("port")+
                "/"+TestReader.config.get("db");
        try {
            connection = DriverManager.getConnection(url,
                    TestReader.config.get("username"),
                    TestReader.config.get("password"));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to establish connection: "+e.getMessage());
        }
    }
}
