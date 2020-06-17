package client;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;


class Connection {
   private DatagramSocket clientSocket;
   InetAddress IPAddress;
   
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


   public Connection(int packetSize) {
      this.packetSize = packetSize;
   }
   
   public void open() throws SocketException, UnknownHostException {
      // declara socket cliente
      clientSocket = new DatagramSocket();

      // obtem endereço IP do servidor com o DNS
      IPAddress = InetAddress.getByName("localhost");

   }

   public void send(String fileName) throws IOException {
      //abre o arquivo
      String fileText= openFile(fileName);
      headerSize = (1 + "\n" + fileName + "\n").length();
      int numberSegments = segmentsNumber(fileText);
      
      for (int i = 0; i < numberSegments ; i++) {
         //get segment 0
         byte[] out = getSegment(fileText, fileName, i);
   
         // cria pacote com o dado, o endereço do server e porta do servidor
         DatagramPacket sendPacket = new DatagramPacket(out, out.length, IPAddress, 1971);

         // envia o pacote
         clientSocket.send(sendPacket);
         System.out.println(yellow +"Pacote enviado" + reset);
      }
      System.out.println(green + "Arquivo enviado" + reset);      
   }

   public String openFile(String fileName) throws IOException {
      File file = new File("in_files/" + fileName);
      FileInputStream fileReader = new FileInputStream(file);
      return new String(fileReader.readAllBytes());
   }

   public void close() {
      // fecha o cliente
      clientSocket.close();
   }

   public byte[] getSegment(String fileText, String fileName, int ack) throws IOException {
      byte[] header = convertText(ack + "\n" + fileName + "\n");
      byte[] content = convertText(fileText);
      byte[] segment = new byte[packetSize];
      int i;
      for (i = 0; i < header.length; i++) segment[i] = header[i];

      for (int j = ack*(segment.length-header.length); j < content.length && i<segment.length ; j++) {
         segment[i] = content[j];
         i++;
      }
      return segment;
   }

   public byte[] convertText(String text) throws IOException {
      return text.getBytes();
   }

   public int segmentsNumber(String packet) {
      double result = (double)packet.length()/(double)(packetSize-headerSize);
      return (int)Math.ceil(result);
   }

   public void breakPackets(int segment) {

   }

}
