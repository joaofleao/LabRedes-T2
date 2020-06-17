import java.io.*;
import java.net.*;

class UDPServer {
      public static void main(String args[]) throws IOException {
            // cria socket do servidor com a porta 1971
            DatagramSocket serverSocket = new DatagramSocket(1971);
            byte[] receiveData = new byte[1024];
            // declara o pacote a ser recebido
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            // recebe o pacote do cliente
            serverSocket.receive(receivePacket);

            // salva os dados recebidos em array de bytes
            byte[] data = receivePacket.getData();

            // cria nome do arquivo
            String fileName = "";

            // pega dos dados o nome do arquivo
            int i;
            for (i = 0; data[i] != 10; i++) {
                  fileName = fileName + (char) data[i];
            }

            // cria o arquivo que vai receber os dados
            File file = new File("out_files/" + fileName);
            FileOutputStream fileWritter = new FileOutputStream(file);

            // escreve os dados no arquivo
            i++;
            while (data[i] != 0) {
                  fileWritter.write(data[i]);
                  i++;
            }

            System.out.println("Arquivo recebido");

      }
}
