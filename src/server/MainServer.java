package server;
import java.io.IOException;
public class MainServer {
    public static void main(String args[]) throws IOException {
        Server s = new Server(23);
        s.open();
        while (true)s.receiveFile();
    }
}