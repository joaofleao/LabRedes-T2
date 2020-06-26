import java.util.zip.CRC32;

public class PacketObject {
    private int number;
    private int numberOfPackets;
    private String name;
    private String content;
    private int packetSize;
    private String crc;

    public PacketObject(int number, String name, int packetSize) {
        this.name = name;
        this.number = number;
        this.packetSize = packetSize;
    }

    public long generateCRC(String input) {
        CRC32 crc = new CRC32();
        crc.update(input.getBytes());
        return crc.getValue();
    }

    private String formatCRC(long crc) {
        String converted = Long.toString(crc);
        for (int i = converted.length(); i < 10; i++) {
            converted = "0" + converted;
        }
        return converted;
    }

    public void setContent(String content) {
        this.content = content;
        this.crc = formatCRC(generateCRC(this.content));
    }

    public void setCRC(String crc) {        
        this.crc = crc;
    }
    public int getNumberOfPackets() {
        return numberOfPackets;
    }

    public void setNumberOfPackets(int numberOfPackets) {
        this.numberOfPackets = numberOfPackets;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getContentSize() {
        return packetSize - ("000\n000\n"+ name +"\n0123456789\n").length();
    }

    public String getContent() {
        return content;
    }

    public String getCRC() {
        return crc;
    }

    public int getNumber() {
        return number;
    }

    private String formatNumber(int unformatted) {
        if (unformatted < 10)
            return "00" + unformatted;
        if (unformatted < 100)
            return "0" + unformatted;
        return unformatted + "";
    }

    public String toString() {
        return formatNumber(number) + "\n" + formatNumber(numberOfPackets) + "\n" + name + "\n" + crc + "\n" + content;
    }
}