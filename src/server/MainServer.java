package server;
import java.io.IOException;
public class MainServer {
    public static void main(String args[]) throws Exception {
        ServerConnection s = new ServerConnection(23);
        s.open();
        while(true) s.receiveFile();
    }
}