package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import utils.PacketObject;

public class ClientFile {
    private String name;
    private String content;
    private ArrayList<PacketObject> packets;

    public ClientFile(String name, int packetSize) throws Exception {
        this.name = name;
        open();
        setPackets(packetSize);

    }

    private String getSegment(int segmentNumber, int segmentSize) {
        String segment = "";
        for (int i = (segmentNumber-1)*segmentSize; segmentSize>0 ; i++) {
            if(i == content.length()) break;
            if(i > content.length()) return "";
            segment = segment + content.charAt(i);
            segmentSize--;
        }
        return segment;
    }

    private void setPackets(int packetSize) {
        packets = new ArrayList<PacketObject>();
        for (int i = 1; true; i++) {
            PacketObject packet = new PacketObject(i, name, "", packetSize);
            String segment = getSegment(i, packet.getContentSize());
            if (segment.length()==0) break;
            packet.setContent(segment);
            packets.add(packet);
        }
        packets.get(packets.size()-1).setNumber(0);
    }

    public int getPacketsSize() {
        return packets.size();
    }

    public String getPacketsItem(int i) {
        return packets.get(i).toString();
    }

    public String getPackets() {
        String teste = "";
        for (PacketObject packetObject : packets) {
            teste = teste + packetObject.toString() + "\n";
        }
        return teste;
    }

    private void open() throws Exception {
        File file = new File("in_files/" + name);
        InputStream is = null;
        is = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        is.read(buffer);
        is.close();
        content = new String (format(buffer));
    }

    public static byte[] format(byte[] packet) {
        String formatted = "";
        for (int i = 0; packet[i] != 0; i++)
            formatted = formatted + (char) packet[i];
        return formatted.getBytes();
    }

}