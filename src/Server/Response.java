package Server;

import JDBC.JDBC;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.concurrent.atomic.AtomicLong;

public class Response {
    // 线程共享变量
    private static AtomicLong id = null;

    // 初始化id 的大小， 防止服务器重启后 id 从头开始计数
    static {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBC.getConnection();
            String sql = "select count(*) from user_info";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
           if(rs.next()) {
               id = new AtomicLong(5120190000L + rs.getInt(1));
           }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.close(conn, stmt, rs);
        }
    }

    // 单例实现
    private static class ResponseSingle {
        private static final Response response = new Response();
    }
    private Response(){}
    public static final Response getInstance() {
        return ResponseSingle.response;
    }

    public void slove(Socket socket) {
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            String query = null;
            do {
                query = in.readUTF();
                if(query.equals("register")) register(socket);
            }while(!query.equals("exit"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 注册函数，判断是否存在注册的用户名，未存在则给当前账户注册
     * @param socket
     */
    private void register(Socket socket) {
        Connection conn = null;
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
            String sql = "select * from user_info where name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                out.writeInt(1);
                return;
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
            // 事务提交
            conn.commit();
            // id增加
            id.getAndAdd(1);
        } catch (SQLException e) {
            e.printStackTrace();
            // 出现异常，事务回滚
            if(conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ee) {
                    ee.printStackTrace();
                }
            }
            // 反馈给客户端信号
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
            // 关闭所有的资源
            JDBC.close(conn, pstmt, rs);
        }
    }
}
