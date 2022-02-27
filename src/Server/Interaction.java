package Server;

import java.net.Socket;

public class Interaction implements Runnable {
    private Socket socket;

    public Interaction(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Response response = Response.getInstance();
        try {
            response.slove(this.socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
