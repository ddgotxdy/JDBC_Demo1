package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;

public class Response {
    // 单例实现
    private static class ResponseSingle {
        private static final Response response = new Response();
    }
    private Response(){}
    public static final Response getInstance() {
        return ResponseSingle.response;
    }

    public void slove(Socket socket) throws Exception{
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        String query = in.readUTF();
        if(query.equals("submit")) submit(socket);
        else {
            out.writeUTF("Error Comand");
        }
        socket.close();
    }

    private void submit(Socket socket) throws Exception {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        String path = File.separator + "root" + File.separator + "java" + File.separator
                + "CS_demo1_tmp" + socket.getInetAddress().toString() + "_" + in.readUTF();
//        String path = "G:" + File.separator + socket.getInetAddress().toString() + "_" + in.readUTF();
        File file = new File(path);
        File fileParent = file.getParentFile();
        if(!fileParent.exists()) {
            fileParent.mkdirs();
        }
        if(!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        do {
            int b = in.read();
            if (b == -1) break;
            fileOutputStream.write(b);
        } while (true);
    }
}
