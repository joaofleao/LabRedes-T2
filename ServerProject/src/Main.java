import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        ServerConnection s = new ServerConnection(512);
        Scanner keyboard = new Scanner(System.in);
        s.open();
        System.out.println("Connection started");
        while(true) {
            System.out.println("Write 0 to receive a file");
            if (keyboard.nextInt()!=0) break;
            s.receiveFile();
        }
        keyboard.close();
        s.close();
        System.out.println("Connection ended");
    }
}


