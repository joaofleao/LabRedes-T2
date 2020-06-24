package server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import utils.PacketObject;

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

      public int getNumber(byte[] text) {
            Scanner scanner = new Scanner(new String(text));
            return Integer.parseInt(scanner.nextLine());

      }

      public void receiveFile() throws Exception {
            ServerFile file = new ServerFile();
            System.out.println(purple + "Waiting..." + reset);

            int iWantToReceive = 1;
            int saved = 0;
            int count = 0;

            while (true) {
                  PacketObject receivedPacket = receivePacket(file);
                  int receivedNumber = receivedPacket.getNumber();
                  if (receivedNumber==saved) count++;
                  else saved = receivedNumber;
                  
                  System.out.println(green + "\nRecebido pacote " + receivedNumber + reset);

                  if (iWantToReceive == receivedNumber) {
                        file.addSegment(receivedPacket);
                        System.out.println(file.getPackets());
                        iWantToReceive++;
                        if (file.isLast(receivedNumber)) break;
                  }
                  else if (count>2) {
                        iWantToReceive = saved;
                        count = 0;
                        System.out.println(saved);
                  }

                  warn(iWantToReceive);
                  System.out.println(yellow + "Eu quero pacote " + iWantToReceive + reset);
            }

            System.out.println(blue + "Arquivo " + file.getName() + " recebido" + reset);

            file.save();
      }

      private void warn(int number) throws IOException {
            byte[] out;
            if (number < 10)
                  out = ("00" + number).getBytes();
            else if (number < 100)
                  out = ("0" + number).getBytes();
            else
                  out = ("" + number).getBytes();

            DatagramPacket sendPacket = new DatagramPacket(out, out.length, IPAddress, 1972);
            serverSocket.send(sendPacket);
      }

      private PacketObject receivePacket(ServerFile file) throws IOException {

            byte[] receiveData = new byte[packetSize];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            return file.getPacketObject(receivePacket.getData());
      }

}