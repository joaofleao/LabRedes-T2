package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SendPackets extends Thread {
   private DatagramSocket socket;
   private String fileText;
   private String fileName;
   private int ack;
   private int segments;
   private int packetSize;
   private InetAddress IPAddress;
   public static final String yellow = "\u001B[33m";
   public static final String reset = "\u001B[0m";

   public SendPackets(DatagramSocket socket, int run, String fileText, String fileName, int ack, int segments, int packetSize) throws UnknownHostException {
      this.socket = socket;

      this.fileText = fileText;
      this.fileName = fileName;
      this.ack = ack;
      this.segments = segments;
      this.packetSize = packetSize;
      IPAddress = InetAddress.getByName("localhost");
   }

   public byte[] getSegment(String fileText, String fileName, int ack, boolean last) throws IOException {
      byte[] header;
      if (last) header = ("00" + "\n" + fileName + "\n").getBytes();
      else if (ack<10)header = ("0" + ack + "\n" + fileName + "\n").getBytes();
      else header = (ack + "\n" + fileName + "\n").getBytes();
      byte[] content = (fileText + "\n").getBytes();
      byte[] segment = new byte[packetSize];
      int i;
      for (i = 0; i < header.length; i++) segment[i] = header[i];

      for (int j = (ack-1)*(segment.length-header.length); j < content.length && i<segment.length ; j++) {
         segment[i] = content[j];
         i++;
      }
      return segment;
   }

   public void run(){
      for (int i = 0; i < 2; i++) {
         try {
            byte[] out = getSegment(fileText, fileName, ack, segments==1);
            DatagramPacket sendPacket = new DatagramPacket(out, out.length, IPAddress, 1971);
            socket.send(sendPacket);
            ack++;
            segments--;
         } catch (IOException e) {
            System.out.println("Erro no envio de pacotes da thread");
         }
      }
      System.out.println(yellow + 2 + " pacotes enviados" + reset);
      
    }
  }