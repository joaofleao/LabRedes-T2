import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        ClientConnection c = new ClientConnection(512);
        Scanner keyboard = new Scanner(System.in);
        c.open();
        System.out.println("Connection started");
        while(true) {
            System.out.println("Write 0 to send a file, or anything else to end connection");
            if (!keyboard.nextLine().equals("0")) break;
            
            
            try {
                System.out.println("Insert the file path");
                c.sendFile(keyboard.nextLine());
            }
            catch(Exception e) {
                System.out.println("Path inexistent");
            }
        }
        keyboard.close();
        c.close();
        System.out.println("Connection ended");      
    }
}