import client.Client;
import java.io.IOException;
public class MainClient {
    public static void main(String args[]) throws IOException {
        Client c = new Client(50);
        c.open();
        c.send("test.txt");
    }
}