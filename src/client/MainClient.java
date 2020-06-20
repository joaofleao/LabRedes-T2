package client;
import java.io.IOException;
public class MainClient {
    public static void main(String args[]) throws IOException {
        Client c = new Client(23);
        c.open();
        c.send("test.txt");
    }
}