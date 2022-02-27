package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String args[]) throws Exception {
        Server server = new Server(6060);
        server.run();
    }

    Server(int port) throws Exception {
        serverSocket = new ServerSocket(port);
    }

    public void run() {
        while(true) {
            try {
                Socket server = serverSocket.accept();
                executorService.submit(new Interaction(server));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}