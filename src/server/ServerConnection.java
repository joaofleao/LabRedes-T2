package server;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class ServerConnection {

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

      public ServerConnection(int packetSize) throws UnknownHostException {
            this.packetSize = packetSize;
            IPAddress = InetAddress.getByName("localhost");
      }

      public void open() throws SocketException, UnknownHostException {
            serverSocket = new DatagramSocket(1971);
      }

      public void close() {
            serverSocket.close();
      }
      
      public void receiveFile() throws Exception {
            ServerFile file = new ServerFile();
            System.out.println(blue + "Waiting" + reset);
            
            byte[] received;
            while (true) {
                  received = receivePacket();
                  System.out.println(yellow + "Pacote " + (char)received[0] + (char)received[1] + (char)received[2] + " recebido" + reset);
                  if (file.addSegment(received)) break; 
            }

            System.out.println(green + "Arquivo " + file.getName() + " recebido" + reset);

            file.save();
      }


      private byte[] receivePacket() throws IOException {
            byte[] receiveData = new byte[packetSize];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            
            return receivePacket.getData();
      }


}