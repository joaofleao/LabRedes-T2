package server;
public class MainServer {
    public static void main(String args[]) throws Exception {
        ServerConnection s = new ServerConnection(44);
        s.open();
        s.receiveFile();
    }
}