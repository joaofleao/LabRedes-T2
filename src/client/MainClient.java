package client;
public class MainClient {
    public static void main(String args[]) throws Exception {
        ClientConnection c = new ClientConnection(44);
        c.open();
        c.sendFile("test.txt");
        
    }
}