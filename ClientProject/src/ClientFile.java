import java.io.*;
import java.util.*;

public class ClientFile {
    private String name;
    private String content;
    private ArrayList<PacketObject> packets;

    public ClientFile(String name, int packetSize) throws Exception {
        this.name = name;
        open();
        setPackets(packetSize);
    }

    private void open() throws Exception {
        File file = new File("../in_files/" + name);
        InputStream is = null;
        is = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        is.read(buffer);
        is.close();
        content = new String(format(buffer));
    }

    private void setPackets(int packetSize) {
        packets = new ArrayList<PacketObject>();
        for (int i = 0; true; i++) {
            PacketObject packet = new PacketObject(i, name, packetSize);
            String segment = getSegment(i, packet.getContentSize());
            if (segment.length() == 0)
                break;
            packet.setContent(segment);
            packets.add(packet);
        }
        for (PacketObject packetObject : packets) {
            packetObject.setNumberOfPackets(packets.size() - 1);
        }
    }

    private String getSegment(int segmentNumber, int segmentSize) {
        String segment = "";
        for (int i = (segmentNumber) * segmentSize; segmentSize > 0; i++) {
            if (i == content.length())
                break;
            if (i > content.length())
                return "";
            segment = segment + content.charAt(i);
            segmentSize--;
        }
        return segment;
    }

    public int getPacketsSize() {
        return packets.size();
    }

    public String getPacketsItem(int i) {
        return packets.get(i).toString();
    }

    public String toString() {
        String text = "";
        for (PacketObject packetObject : packets) {
            text = text + packetObject.toString() + "\n----------------------\n";
        }
        return text;
    }

    public byte[] format(byte[] packet) {
        String formatted = "";
        for (int i = 0; packet[i] != 0; i++)
            formatted = formatted + (char) packet[i];
        return formatted.getBytes();
    }

}