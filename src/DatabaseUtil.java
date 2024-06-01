import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://140.119.19.73:3315/112306079";
    private static final String USER = "112306079";
    private static final String PASSWORD = "jkofu";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
