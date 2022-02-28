package Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {
    private static int port = 6060;
    private static Scanner scanner = new Scanner(System.in);
    private static DataInputStream in = null;
    private static DataOutputStream out = null;
    private static Socket client = null;
    private static String name = null;
    private static String password = null;
    // 访问的ip地址
//    private static String Servername = "182.92.216.59";

    /**
     * 连接服务器
     * @param args
     */

    public static void main(String args[]) {
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
            System.out.println("=======================图书管理系统====================\n\n" +
                               "=================输入数字进入对应功能=====================\n" +
                    "1: register 注册用户" +
                    "2: login    登录账号" +
                    "3: ");

            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());

            int id = 0;
            do {
                id = scanner.nextInt();
                scanner.nextLine();
                switch (id) {
                    case 1:
                        register();
                        break;
                    case 2:

                        break;
                    case 3:
                        break;
                }
            }while(id != 0);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void register() throws IOException {
        out.writeUTF("register");
        System.out.print("请输入账号：");
        name = scanner.nextLine();
        System.out.print("请输入密码：");
        password = scanner.nextLine();

        out.writeUTF(name);
        out.writeUTF(password);
        int res = in.readInt();
        switch (res) {
            case 0:
                System.out.println("注册成功");
                break;
            case 1:
                System.out.println("注册失败 - 用户名存在");
                break;
            case 2:
                System.out.println("注册失败");
                break;
        }
    }

}