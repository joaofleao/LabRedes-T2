
import java.io.*;
import java.net.*;
import java.util.*;


public class ServerConnection {

      private DatagramSocket serverSocket;
      private InetAddress IPAddress;
      private int packetSize;

      public ServerConnection(int packetSize) throws UnknownHostException {
            this.packetSize = packetSize;
            IPAddress = InetAddress.getByName("localhost");
      }

      public void open() throws SocketException, UnknownHostException {
            serverSocket = new DatagramSocket(1971);
            serverSocket.setSoTimeout(10000);
      }

      public void close() {
            serverSocket.close();
      }

      public void receiveFile() throws Exception {
            ServerFile file = new ServerFile();
            System.out.println(Variables.purple + "Waiting..." + Variables.reset);

            int iNeed = 0;
            int received = 0;
            int count = 0;

            while (true) {
                  PacketObject receivedPacket;
                  try {
                        receivedPacket = receivePacket(file);
                        System.out.println(Variables.blue + "Transmission Started " + Variables.reset);
                  } catch (Exception e) {
                        System.out.println(Variables.blue + "Client did not started transmission" + Variables.reset);
                        return;
                  }
                  
                  
                  System.out.println(Variables.green + "\nPacket" + receivedPacket.getNumber() + " received"
                              + Variables.reset);
                  if (received == receivedPacket.getNumber())
                        count++;
                  else
                        received = receivedPacket.getNumber();
                  if (received == iNeed) {

                        file.addSegment(receivedPacket);
                        iNeed++;
                  }

                  if (count == 3) {
                        iNeed = received;
                        count = 0;
                  }

                  warn(iNeed);
                  System.out.println(Variables.yellow + "\nPacket" + iNeed + " asked" + Variables.reset);
                  if (iNeed - 1 == receivedPacket.getNumberOfPackets())
                        break;
            }

            System.out.println(Variables.blue + "File " + file.getName() + " received and saved" + Variables.reset);
            file.save();

            System.out.println(Variables.blue + "Waiting for Client to transmission " + Variables.reset);
            try {
                  receiveTransmissionEnd();
                  System.out.println(Variables.blue + "Transmission is over " + Variables.reset);
            } catch (Exception e) {
                  System.out.println(Variables.blue + "Client not responding, transmission over" + Variables.reset);
            }

      }

      private void receiveTransmissionEnd() throws IOException {
            while (true) {
                  byte[] receiveData = new byte[3];
                  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                  serverSocket.receive(receivePacket);
                  String textReceived = new String(receivePacket.getData());
                  Scanner text = new Scanner(textReceived);
                  warn(Integer.parseInt(text.nextLine()) + 1);
                  text.close();
                  System.out.println(Variables.red + "Client tried to send useless packets" + Variables.reset);

                  if (textReceived.equals("end"))
                        break;
            }
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