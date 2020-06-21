package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileObject {
    private String name;
    private String content;
    private ArrayList<PacketObject> packets;

    public FileObject(String name, int packetSize) throws Exception {
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
            PacketObject packet = new PacketObject(i, name, packetSize);
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

    private void open() throws FileNotFoundException {
        File file = new File("in_files/" + name);
        Scanner fileScanner = new Scanner(file);
        content = fileScanner.nextLine();
        while (fileScanner.hasNextLine()) {
            content = content + "\n" + fileScanner.nextLine();
        }
    }

}