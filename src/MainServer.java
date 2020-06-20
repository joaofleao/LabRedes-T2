import server.Server;
import java.io.IOException;
public class MainServer {
    public static void main(String args[]) throws IOException {
        Server s = new Server(50);
        s.open();
        while (true)s.receive();
    }
}