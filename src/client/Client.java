package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;


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

   public String openFile(String fileName) throws IOException {
      File file = new File("in_files/" + fileName);
      Scanner fileScanner = new Scanner(file);
      String content = fileScanner.nextLine();
      while (fileScanner.hasNextLine()) {
         content = content + "\n" + fileScanner.nextLine() ;
      }
      return content;
   }

   public byte[] convertText(String text) throws IOException {
      return text.getBytes();
   }

   
   public int numberOfSegments(String packet, int headerSize) {
      double result = (double)packet.length()/(double)(packetSize-headerSize);
      return (int)Math.ceil(result);
   }

   public byte[] getSegment(String fileText, String fileName, int ack, boolean last) throws IOException {
      byte[] header;
      if (last) header = convertText("00" + "\n" + fileName + "\n");
      else if (ack<10)header = convertText("0" + ack + "\n" + fileName + "\n");
      else header = convertText(ack + "\n" + fileName + "\n");
      byte[] content = convertText(fileText + "\n");
      byte[] segment = new byte[packetSize];
      int i;
      for (i = 0; i < header.length; i++) segment[i] = header[i];

      for (int j = (ack-1)*(segment.length-header.length); j < content.length && i<segment.length ; j++) {
         segment[i] = content[j];
         i++;
      }
      return segment;
   }

   public void send(String fileName) throws IOException {
      //abre o arquivo
      String fileText= openFile(fileName);
      int headerSize = (fileName).length() +6;
      int numberSegments = numberOfSegments(fileText, headerSize);
      
      int ack = 1;
      for (int j = 1; numberSegments>0; j = j*2) {
         if (j>numberSegments) j = numberSegments;
         for (int i = 0; i < j ; i++) {
            byte[] out = getSegment(fileText, fileName, ack, (numberSegments==1));
            DatagramPacket sendPacket = new DatagramPacket(out, out.length, IPAddress, 1971);
            clientSocket.send(sendPacket);
            ack++;
            numberSegments--;
         }
         System.out.println(yellow + j + " pacotes enviado" + reset);
      }

      System.out.println(green + "Arquivo enviado" + reset);      
   }

}
