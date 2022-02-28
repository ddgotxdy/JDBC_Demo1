package JDBC;

import java.sql.*;
import java.util.ResourceBundle;

public class JDBC {

    private static ResourceBundle bundle = null;
    private static String username = null;
    private static String password = null;
    private static String JDBC_DRIVER = null;
    private static String DB_URL = null;

    static {
        // 使用资源绑定器绑定属性配置文件
        bundle = ResourceBundle.getBundle("jdbc");
        username = bundle.getString("username");
        password = bundle.getString("password");
        JDBC_DRIVER = bundle.getString("JDBC_DRIVER");
        DB_URL = bundle.getString("DB_URL_localhost");
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private JDBC(){}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, username, password);
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
