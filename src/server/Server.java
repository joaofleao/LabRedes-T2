package server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {

      private DatagramSocket serverSocket;
      private InetAddress IPAddress;
      private int packetSize;
      private int headerSize;
      private String fileName;

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
      }

      public byte[] assemblePackets(byte[] packetA, byte[] packetB) {
            
            String textA = new String(packetA);
            String textB = new String(packetB);
            Scanner reader = new Scanner(textB);

            reader = new Scanner(textB);
            reader.nextLine();           
            fileName = reader.nextLine();           
            textB = reader.nextLine();
            
            return (textA+textB).getBytes();
      }




      public void receive() throws IOException {
            byte[] receiveData = new byte[packetSize];
            
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            System.out.println(blue + "Waiting" + reset);

            byte[] assembled = ("").getBytes();
            while(true) {
                  serverSocket.receive(receivePacket);
                  byte[] received = receivePacket.getData(); 
                  assembled = assemblePackets(assembled, received);
                  System.out.println(yellow +"Pacote recebido" + reset);
                  if (received[0]==48) break;
            }
            System.out.println(green + "Arquivo recebido" + reset);

            saveFile(fileName, new String(assembled));

            System.out.println(green + "Arquivo salvo" + reset);
                  
      }


}
