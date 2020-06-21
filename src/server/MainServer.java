package server;
import java.io.IOException;
public class MainServer {
    public static void main(String args[]) throws Exception {
        Server s = new Server(23);
        s.open();
        s.receiveFile();
    }
}