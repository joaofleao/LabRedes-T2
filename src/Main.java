import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) throws IOException {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Digite o nome do arquivo:");
        String fileName = keyboard.nextLine();
        
        
        UDPConnection c = new UDPConnection(1971);
        c.open();
        c.send("test.txt");
    }
    
}