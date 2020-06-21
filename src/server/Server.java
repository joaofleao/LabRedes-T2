package server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.rmi.ssl.SslRMIClientSocketFactory;

public class Server {

      private DatagramSocket serverSocket;
      private InetAddress IPAddress;
      private int packetSize;
      private int headerSize;

      public static final String reset = "\u001B[0m";
      public static final String black = "\u001B[30m";
      public static final String red = "\u001B[31m";
      public static final String green = "\u001B[32m";
      public static final String yellow = "\u001B[33m";
      public static final String blue = "\u001B[34m";
      public static final String purple = "\u001B[35m";
      public static final String cyan = "\u001B[36m";
      public static final String white = "\u001B[37m";

      public Server(int packetSize) throws UnknownHostException {
            this.packetSize = packetSize;
            IPAddress = InetAddress.getByName("localhost");
      }

      public void open() throws SocketException, UnknownHostException {
            serverSocket = new DatagramSocket(1971);
      }

      public void close() {
            serverSocket.close();
      }

      public void saveFile(String fileName, String text) throws IOException {
            File file = new File("out_files/" + fileName);
            FileOutputStream fileWritter = new FileOutputStream(file);
            fileWritter.write(text.getBytes());
            System.out.println(green + "Arquivo salvo" + reset);
      }

      public String formatReceived(byte[] packet) {
            int i;
            for (i = 4; packet[i]!=10; i++);
            i++;
            String formatted = "";
            while(packet.length>i&& packet[i]!=0 ) {
                  formatted = formatted + (char)packet[i];
                  i++;
            }
            return formatted;
      }
      public String getName(byte[] packet) {
            String name = new String(packet);
            Scanner reader = new Scanner(name);
            reader.nextLine();
            return reader.nextLine();

      }

      public void receiveFile() throws IOException {
            System.out.println(blue + "Waiting" + reset);
            
            byte[] received;
            String assembled = "";
            do {
                  received = receivePacket();
                  assembled = assembled + formatReceived(received);
            } while (!(received[0] == 48 && received[1]==48 && received[2]==48));

            System.out.println(green + "Arquivo recebido" + reset);

            saveFile(getName(received), assembled);
      }


      private byte[] receivePacket() throws IOException {
            byte[] receiveData = new byte[packetSize];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            
            System.out.println(yellow + "Pacote " + (char)receivePacket.getData()[0] + (char)receivePacket.getData()[1] + (char)receivePacket.getData()[2] + " recebido" + reset);
            return receivePacket.getData();
      }


}