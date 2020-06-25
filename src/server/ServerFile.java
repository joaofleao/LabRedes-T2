package server;

import java.io.*;
import java.util.*;
import utils.Variables;

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

    public PacketObject getPacketObject(byte[] segment) {
        String number = "";
        String size = "";
        String packetName = "";
        String crc = "";
        String packetContent = "";

        int i;

        for (i = 0; segment[i] != 10; i++)
            number = number + (char) segment[i];

        for (i++; segment[i] != 10; i++)
            size = size + (char) segment[i];

        for (i++; segment[i] != 10; i++)
            packetName = packetName + (char) segment[i];

        for (i++; segment[i] != 10; i++)
            crc = crc + (char) segment[i];

        for (i++; segment.length > i && segment[i] != 0; i++)
            packetContent = packetContent + (char) segment[i];

        PacketObject packet = new PacketObject(Integer.parseInt(number), packetName, segment.length);
        packet.setNumberOfPackets(Integer.parseInt(size));
        packet.setContent(packetContent);
        packet.setCRC(crc);
        name = packetName;

        return packet;
    }

    private boolean testCRC(PacketObject packet) {
        long crcreceived = Long.parseLong(packet.getCRC());
        long crcMade = packet.generateCRC(packet.getContent());

        if (crcreceived == crcMade)
            return true;

        return false;
    }

    public boolean addSegment(PacketObject packet) {
        if (!testCRC(packet)) {
            System.out.println(Variables.red + "CRC incorrect" + Variables.reset);
            return false;
        }
        if (packet.getNumber() == packets.size()) {
            packets.add(packet);
            System.out.println(Variables.cyan + "Segment added" + Variables.reset);
        } else {
            System.out.println(Variables.yellow + "Segmento already added" + Variables.reset);
        }

        return true;

    }

    public void save() throws Exception {
        assembleSegments();
        File file = new File("out_files/" + name);
        FileOutputStream fileWritter = new FileOutputStream(file);
        fileWritter.write(content.getBytes());
        fileWritter.close();
    }

    public String getPackets() {
        String teste = "";
        for (PacketObject packetObject : packets) {
            teste = teste + packetObject.getContent();
        }
        return teste;
    }

}