package database;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class Connection {
    public static java.sql.Connection connect(String url, String username, String password) {

        String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";

        //STEP 1: Get DB credentials
        String DB_URL = url;
        String USER = username;
        String PASS = password;

        java.sql.Connection conn = null;

        //STEP 2: Register JDBC driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //STEP 3: Open a connection
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static Statement statement(java.sql.Connection conn) {
        Statement stmt = null;
        //STEP 4: Create Statement
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stmt;
    }

    public static String executeOneResultQuery(Statement stmt, String query, String column) {
        ResultSet rs;
        String queryColumn = null;
        if (query != null && !query.isEmpty() && query != " " && query.substring(0, 1) != "--") {
            try {
                rs = stmt.executeQuery(query);
                while (rs.next()) {
                    queryColumn = rs.getString(column);
                }
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return queryColumn;
    }

    public static void executeUpdateQuery(Statement stmt, String query) {
        ResultSet rs;
        if (query != null && !query.isEmpty() && query != " " && query.substring(0, 1) != "--") {
            try {
                rs = stmt.executeQuery(query);
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void commit(Statement stmt) {
        String query = "Commit";
        ResultSet rs;
        if (query != null && !query.isEmpty() && query != " " && query.substring(0, 1) != "--") {
            try {
                rs = stmt.executeQuery(query);
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void disconnect(Statement stmt, java.sql.Connection conn) {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
