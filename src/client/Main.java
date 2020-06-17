package client;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) throws IOException {
        //Scanner keyboard = new Scanner(System.in);
        //System.out.println("Digite o nome do arquivo:");
        //String fileName = keyboard.nextLine();
        
        
        Connection c = new Connection(20);
        //c.open();
        c.send("test.txt");
        
    }
    
}