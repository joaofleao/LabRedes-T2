import java.io.*;
import java.net.*;
import java.util.*;


class UDPConnection {
   private DatagramSocket clientSocket;
   InetAddress IPAddress;
   
   public UDPConnection(int port) {
      

   }
   public FileInputStream openFile(String fileName) throws FileNotFoundException {
      File file = new File("in_files/" + fileName);
      FileInputStream fileReader = new FileInputStream(file);
      return fileReader;
   }
   
   public byte[] convertFile(FileInputStream fileReader, String fileName) throws IOException {
      // criar array pra enviar dados
      ByteArrayOutputStream sendData = new ByteArrayOutputStream();

      // adicionar o nome do arquivo aos dados a serem enviados
      sendData.write((fileName + "\n").getBytes());

      // adicionar o conteudo do arquivo aos dados a serem enviados
      for (int i = fileReader.read(); i != -1; i = fileReader.read()) {
         sendData.write(i);
      }

      // transformar dados em array de 1024 de tamanho
      byte[] out = new byte[1024];
      out = sendData.toByteArray();
      return out;
      
   }

   public void open() throws SocketException, UnknownHostException {
      // declara socket cliente
      clientSocket = new DatagramSocket();

      // obtem endereço IP do servidor com o DNS
      IPAddress = InetAddress.getByName("localhost");

   }

   public void send(String fileName) throws IOException {
      //abre o arquivo
      FileInputStream file= openFile(fileName);

      //converte o arquivo
      byte[] out = convertFile(file, fileName);

      // cria pacote com o dado, o endereço do server e porta do servidor
      DatagramPacket sendPacket = new DatagramPacket(out, out.length, IPAddress, 1971);

      // envia o pacote
      clientSocket.send(sendPacket);

      //notifica que o arquivo foi enviado
      System.out.println("Arquivo enviado");
      
   }

   public void close() {
      // fecha o cliente
      clientSocket.close();
   }
}
