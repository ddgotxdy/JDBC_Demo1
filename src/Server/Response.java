package Server;

import JDBC.JDBC;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.concurrent.atomic.AtomicLong;

public class Response {
    private static AtomicLong id = new AtomicLong(5120190000L);

    // 单例实现
    private static class ResponseSingle {
        private static final Response response = new Response();
    }
    private Response(){}
    public static final Response getInstance() {
        return ResponseSingle.response;
    }

    public void slove(Socket socket) throws Exception {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        String query = null;
        do {
            query = in.readUTF();
            if(query.equals("register")) register(socket);
        }while(!query.equals("exit"));
        socket.close();
    }

    private void register(Socket socket) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // 判断是否出现当前注册用户名
            String name = in.readUTF();
            String password = in.readUTF();
            conn = JDBC.getConnection();
            String sql = "select name from user_info";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                System.out.println(rs.getString("name"));
                if(rs.getString("name").equals(name)) {
                    out.writeInt(1);
                    return;
                }
            }
            // 开始注册
            conn.setAutoCommit(false);
            sql = "insert into user_info values(?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id.toString());
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            int count = pstmt.executeUpdate();

            if(count == 1) {
                out.writeInt(0);
            }else {
                out.writeInt(1);
            }
            conn.commit();
            id.getAndAdd(1);
        } catch (SQLException e) {
            e.printStackTrace();
            if(conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ee) {
                    ee.printStackTrace();
                }
            }
            if(out != null) {
                try {
                    out.writeInt(2);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            JDBC.close(conn, stmt, rs);
            JDBC.close(conn, pstmt, rs);
        }

    }
}
