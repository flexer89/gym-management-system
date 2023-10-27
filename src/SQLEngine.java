import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLEngine {

    private Connection connection;
    private String url;
    private String username;
    private String password;

    public SQLEngine(String host, int port, String database, String username, String password) throws SQLException {
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.username = username;
        this.password = password;
        this.connection = null;
    }

    public synchronized Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void executeQuery(String query) throws SQLException {
        connection.createStatement().executeQuery(query);
    }

    public void executeUpdate(String query) throws SQLException {
        connection.createStatement().executeUpdate(query);
    }
}
