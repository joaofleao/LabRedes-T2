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
      int iWantToSend = 1;
      int send = 1;
      int shallwe = 1;
      
      while (iWantToSend < file.getPacketsSize()) {
         if (shallwe == file.getPacketsSize()+1) {
            send = 0;
            System.out.println(shallwe);
         }
         if (shallwe == file.getPacketsSize()) {
            send = 1;
            System.out.println(shallwe);
         }
         if (shallwe == file.getPacketsSize()-1) {
            send = 2;
            System.out.println(shallwe);
         }

         if (send==1) {
            sendPacket(file.getPacketsItem(shallwe));
            System.out.println(yellow + "\nSingle Mandei o pacote " + shallwe + reset);
            send=2;
            shallwe++;
            if (shallwe == file.getPacketsSize()) {
               send = 1;
            }
            else if (shallwe == file.getPacketsSize()+1) {
               send = 0;
            }
         }
         else if (send==2){
            for (int i = 0; i < 2 && iWantToSend < file.getPacketsSize(); i++) {
               sendPacket(file.getPacketsItem(shallwe));
               System.out.println(yellow + "\nDouble Mandei o pacote " + shallwe + reset);
               shallwe++;
            }
         }
       
         int iWillSend = receiveConfirmation(iWantToSend);
         if (iWillSend - 1 == iWantToSend) {
            System.out.println(green + "Pacote " + iWantToSend + " confirmado" + reset);
            System.out.println(new String(file.getPacketsItem(iWantToSend).getBytes()));
            iWantToSend = iWillSend;
         }
         else {
            shallwe = iWillSend;
            System.out.println(red + shallwe + " " +iWillSend + reset);
            send = 1;
         }

      }

      System.out.println(blue + "Arquivo " + fileName + " enviado" + reset);
   }

   private int receiveConfirmation(int iWantToSend) throws IOException {
      byte[] receivedData = new byte[3];
      DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
      for(int tentativas = 0; tentativas<3; tentativas++) {
         try {
            clientSocket.receive(receivedPacket);
            int receivedNumber = Integer.parseInt(new String(receivedPacket.getData()));
            //System.out.println(cyan + "Pacote " + receivedNumber + " recebido" + reset);
            if (receivedNumber-1==iWantToSend) return receivedNumber;
            //else System.out.println(red + "Verificando pela " + receivedNumber + " eu ia mandar " + iWantToSend + reset);
         } catch (SocketTimeoutException e) {
            System.out.println(red + "TimeOut, solicitando " + iWantToSend + " novamente" + reset);
            //break;
         }
      }
      
      return iWantToSend;

   }

   private void sendPacket(String fileText) throws IOException {
      byte[] out = fileText.getBytes();
      DatagramPacket sendPacket = new DatagramPacket(out, out.length, IPAddress, 1971);
      clientSocket.send(sendPacket);
   }

}
