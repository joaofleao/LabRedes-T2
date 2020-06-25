package client;

import java.io.*;
import java.net.*;


import utils.Variables;

public class ClientConnection {

   private DatagramSocket clientSocket;
   private InetAddress IPAddress;
   private int packetSize;

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

      int confirmed = 0;
      int sending = 0;
      int sendQuantity = 1;
      while (confirmed < file.getPacketsSize()) {

         if (sendQuantity > 0 && sending < file.getPacketsSize()) {
            sendPacket(file.getPacketsItem(sending));
            System.out.println(Variables.yellow + "Packet " + sending + " sent" + Variables.reset);
            sending++;
         }

         if (sendQuantity == 2 && sending < file.getPacketsSize()) {
            sendPacket(file.getPacketsItem(sending));
            System.out.println(Variables.yellow + "Packet " + sending + " sent" + Variables.reset);
            sending++;
         }

         if (receivePacket(confirmed)) {
            System.out.println(Variables.green + "Packet " + confirmed + " confirmed" + Variables.reset);
            confirmed++;
            sendQuantity = 2;

         } else {
            sending = confirmed;
            sendQuantity = 1;
         }
      }


      System.out.println(Variables.blue + "File " + fileName + " sent" + Variables.reset);

      finishTransmission();
      System.out.println(Variables.blue + "Transmission finished" + Variables.reset);
   }

   private void finishTransmission() throws IOException {
      byte[] out = "end".getBytes();
      DatagramPacket sendPacket = new DatagramPacket(out, out.length, IPAddress, 1971);
      clientSocket.send(sendPacket);
   }

   private boolean receivePacket(int confirmed) throws IOException {
      byte[] receivedData = new byte[3];
      DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);

      for (int tries = 0; tries < 3; tries++) {
         try {
            clientSocket.receive(receivedPacket);
            int receivedNumber = Integer.parseInt(new String(receivedPacket.getData()));

            if (receivedNumber - 1 == confirmed)
               return true;
         } catch (SocketTimeoutException e) {
            System.out.println(Variables.red + "TimeOut, asking for " + confirmed + " again" + Variables.reset);
            return false;
         }
      }
      System.out.println(Variables.red + "MisMatch, asking for " + confirmed + " again" + Variables.reset);
      return false;

   }

   private void sendPacket(String fileText) throws IOException {
      byte[] out = fileText.getBytes();
      DatagramPacket sendPacket = new DatagramPacket(out, out.length, IPAddress, 1971);
      clientSocket.send(sendPacket);
   }

}
