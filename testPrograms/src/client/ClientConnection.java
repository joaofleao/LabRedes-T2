package client;

import java.io.*;
import java.net.*;

public class ClientConnection {

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

   public ClientConnection(int packetSize) throws UnknownHostException {
      this.packetSize = packetSize;
      IPAddress = InetAddress.getByName("localhost");
   }

   public void open() throws SocketException {
      clientSocket = new DatagramSocket(1972);
      clientSocket.setSoTimeout(1000);
   }

   public void close() {
      clientSocket.close();
   }

   public void sendFile(String fileName) throws Exception {
      ClientFile file = new ClientFile(fileName, packetSize);
      int sended = 1;
      int seq = 1;
      for (int i = 0; sended < file.getPacketsSize(); i++) {
         if (seq==0) seq = 1;
         if(seq<file.getPacketsSize()){
            sendPacket(file.getPacketsItem(seq-1));
            System.out.println(yellow + "Pacote " + seq + " enviado" + reset);
            seq++;
         }
         if (i != 0 && seq<file.getPacketsSize()) {
            i++;
            sendPacket(file.getPacketsItem(seq-1));
            System.out.println(yellow + "Pacote " + seq + " enviado" + reset);
            seq++;
         }
         if (receiveConfirmation(sended))
            sended++;
         else {
            i = -1;
            seq = sended-1;
            System.out.println();
         }
      }

      System.out.println(green + "Arquivo " + fileName + " enviado" + reset);
   }

   private boolean receiveConfirmation(int sended) throws IOException {
      byte[] receivedData = new byte[3];
      DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);

      for (int i = 1; i <= 3; i++) {
         try {
            clientSocket.receive(receivedPacket);
            int received = Integer.parseInt(new String(receivedData));
            if (received == sended) {
               System.out.println(green + "Packet " + sended + " confirmed" + reset);
               return true;
            }
            System.out.println(red + "Packet " + received + " confirmed instead of " + sended + ", attempt: " + i + reset);
         } catch (SocketTimeoutException e) {
            System.out.println(red + "No response, resending..." + reset);
         }

      }

      System.out.println(red + "Officialy not confirmed, resending..." + reset);
      return false;
   }

   private void sendPacket(String fileText) throws IOException {
      byte[] out = fileText.getBytes();
      DatagramPacket sendPacket = new DatagramPacket(out, out.length, IPAddress, 1971);
      clientSocket.send(sendPacket);
   }

}
