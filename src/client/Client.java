package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import model.FileObject;

public class Client {
   
   private DatagramSocket clientSocket;
   private InetAddress IPAddress;
   private int packetSize;

   public static final String reset = "\u001B[0m";
   public static final String black = "\u001B[30m";
   public static final String red = "\u001B[31m";
   public static final String green = "\u001B[32m";
   public static final String yellow = "\u001B[33m";
   public static final String blue = "\u001B[34m";
   public static final String purple = "\u001B[35m";
   public static final String cyan = "\u001B[36m";
   public static final String white = "\u001B[37m";

   public Client(int packetSize) throws UnknownHostException {
      this.packetSize = packetSize;
      IPAddress = InetAddress.getByName("localhost");
   }
   
   public void open() throws SocketException{
      clientSocket = new DatagramSocket(1972);     
   }
   
   public void close() {
      clientSocket.close();
   }

   public void sendFile(String fileName) throws Exception {
      FileObject file = new FileObject(fileName, packetSize);
      
      for (int i = 0; i<file.getPacketsSize(); i++) {
         sendPacket(file.getPacketsItem(i));
         System.out.println(yellow + "Pacote " + (i+1) + " enviado" + reset);
         System.out.println(yellow + file.getPacketsItem(i) + reset);

         if (i+1<file.getPacketsSize() && i!=0) {
            i++;
            sendPacket(file.getPacketsItem(i));
            System.out.println(yellow + "Pacote " + (i+1) + " enviado" + reset);
         }
         //receiveConfirmation();
      }
      
      System.out.println(green + "Arquivo enviado" + reset);      
   }

   // private boolean receiveConfirmation() throws IOException {

   //    byte[] receivedData = new byte[1024];
   //    DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
   //    clientSocket.receive(receivedPacket);
   //    return true;

   // }

   private void sendPacket(String fileText) throws IOException {
      byte[] out = fileText.getBytes();
      DatagramPacket sendPacket = new DatagramPacket(out, out.length, IPAddress, 1971);
      clientSocket.send(sendPacket);
   }

}
