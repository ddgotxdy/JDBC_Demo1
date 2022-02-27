package Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {
    private static int port = 6060;
    private static Scanner scanner = new Scanner(System.in);
    // 访问的ip地址
//    private static String Servername = "182.92.216.59";

    public static void main(String args[]) throws Exception {
        try {
//            本地测试
            InetAddress inetAddress = InetAddress.getLocalHost();
            Socket client = new Socket(inetAddress.getHostName(),port);
//            远程服务器测试，需要配置内网穿透
//            Socket client = new Socket(Servername,port);
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            File file = null;
            do {
                String path = scanner.nextLine();
                file = new File(path);
                if(file.isFile()) break;
                else System.out.println("Error File name or is directory");
            }while(true);
            FileInputStream fileInputStream = new FileInputStream(file);

            out.writeUTF("submit");
            out.writeUTF(file.getName());
            int b;
            do {
                b = fileInputStream.read();
                out.write(b);
            }while(b != -1);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}