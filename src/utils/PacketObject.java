package utils;

public class PacketObject {
    private int number;
    private String name;
    private String content;
    private int packetSize;

    public PacketObject(int number, String name, String content, int packetSize) {
        this.name = name;
        this.number = number;
        this.packetSize = packetSize;
        this.content = content;
    }

    public PacketObject(int parseInt, String packetName, String packetContent) {
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getContentSize() {
        return packetSize - ("000" + "\n" + name + "\n" + "\n").length();
    }

    public String getContent() {
        return content;
    }

    public String toString() {
        if (number < 10)
            return "00" + number + "\n" + name + "\n" + content;
        if (number < 100)
            return "0" + number + "\n" + name + "\n" + content;
        return number + "\n" + name + "\n" + content;
    }
}