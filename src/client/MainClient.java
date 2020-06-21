package client;

import java.io.IOException;
import java.net.SocketException;
public class MainClient {
    public static void main(String args[]) throws Exception {
        Client c = new Client(23);
        c.open();
        c.sendFile("test.txt");
        
    }
}