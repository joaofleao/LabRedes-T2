package server;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import utils.PacketObject;

public class ServerFile {
    private String name;
    private String content;

    private ArrayList<PacketObject> packets;

    public ServerFile() throws Exception {
        packets = new ArrayList<PacketObject>();
    }

    public String getName() {
        return name;
    }

    private void assembleSegments() {
        content = "";
        for (PacketObject packetObject : packets) {
            content = content + packetObject.getContent();
        }

    }

    public boolean addSegment(byte[] segment) {
        String packetNumber = "";
        name = "";
        String packetContent = "";
        int i;

        for (i = 0; segment[i] != 10; i++)
            packetNumber = packetNumber + (char) segment[i];

        for (i++; segment[i] != 10; i++)
            name = name + (char) segment[i];

        for (i++; segment.length > i && segment[i] != 0; i++)
            packetContent = packetContent + (char) segment[i];

        PacketObject packet = new PacketObject(Integer.parseInt(packetNumber), name, packetContent, segment.length);

        packets.add(packet);

        if (Integer.parseInt(packetNumber) == 0)
            return true;
        return false;
    }

    public void save() throws Exception {
        assembleSegments();
        File file = new File("out_files/" + name);
        FileOutputStream fileWritter = new FileOutputStream(file);
        fileWritter.write(content.getBytes());
    }

    public String getPackets() {
        String teste = "";
        for (PacketObject packetObject : packets) {
            teste = teste + packetObject.toString() + "\n";
        }
        return teste;
    }

}