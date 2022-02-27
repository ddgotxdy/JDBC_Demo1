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

    /**
     * 连接服务器
     * @param args
     */

    public static void main(String args[]) {
        Socket client = null;
        try {
//            本地测试
            InetAddress inetAddress = InetAddress.getLocalHost();
            client = new Socket(inetAddress.getHostName(),port);
//            远程服务器测试，需要配置内网穿透
//            Socket client = new Socket(Servername,port);
            interaction(client);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void interaction(Socket client) {
        try {
            System.out.println("=======================图书管理系统====================\n" +
                    "1：");

            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());

            int id = 0;
            do {
                id = in.readInt();
                switch (id) {
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:
                        break;
                }
            }while(id != 0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void register() {

    }


}