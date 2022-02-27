package JDBCStudy;

import java.sql.*;
import java.util.ResourceBundle;

public class JDBC {

    public static void main(String[] args) {
        // 使用资源绑定器绑定属性配置文件
        ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
        String username = bundle.getString("username");
        String password = bundle.getString("password");
        String JDBC_DRIVER = bundle.getString("JDBC_DRIVER");
        String DB_URL = bundle.getString("DB_URL");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // 第一步
            Class.forName(JDBC_DRIVER);
            // 第二步
            conn = DriverManager.getConnection(DB_URL, username, password);
            // 第三步
            String sql = "select * from user_info where name like ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "d%");
            // 第四步
            rs = stmt.executeQuery();
            // 第五步
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                System.out.println(id + " " + name + " " + age);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 第六步
            try {
                if(rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
