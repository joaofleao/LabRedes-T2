package server;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import utils.PacketObject;

public class ServerFile {
    private String name;
    private String content;
    private int last;

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
    public boolean isLast(int number) {
        return last==number;
    }

    public PacketObject getPacketObject(byte[] segment) {
        String packetNumber = "";
        String temporaryName = "";
        String packetContent = "";
        int i;

        for (i = 0; segment[i] != 10; i++)
            packetNumber = packetNumber + (char) segment[i];

        for (i++; segment[i] != 10; i++)
            temporaryName = temporaryName + (char) segment[i];
        
        if (temporaryName.length()==1) last = Integer.parseInt(packetNumber);
        else name = temporaryName;

        for (i++; segment.length > i && segment[i] != 0; i++)
            packetContent = packetContent + (char) segment[i];

        return new PacketObject(Integer.parseInt(packetNumber), name, packetContent, segment.length);
    }

    // public void addSegment(byte[] segment) {
    //     PacketObject packet = getPacketObject(segment);
    //     packets.add(packet);
    // }

    public void addSegment(PacketObject packet) {
        if (packet.getNumber()==0) packets.add( packet);
        else if (packet.getNumber()>=packets.size())packets.add(packet);
        else  {
            packets.remove(packet.getNumber());
            packets.add(packet.getNumber(), packet);
            
        }
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
            teste = teste + packetObject.getContent();
        }
        return teste;
    }

}