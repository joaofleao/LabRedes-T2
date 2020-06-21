package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class test {
    public static void main(String args[]) throws IOException {
        byte[] old = open("in_files/test.txt");
        save("out_files/test.txt", old);

    }

    private static byte[] open(String name) throws IOException {
        File file = new File(name);
        InputStream is = null;
        is = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        is.read(buffer);
        ;
        is.close();
        return buffer;
    }

    public static byte[] formatReceived(byte[] packet) {
        String formatted = "";
        for (int i = 0; packet[i] != 0; i++)
            formatted = formatted + (char) packet[i];
        return formatted.getBytes();
    }

    private static void save(String name, byte[] received) throws IOException {
        File file = new File(name);
        OutputStream os = null;
        os = new FileOutputStream(file);

        received = formatReceived(received);

        os.write(received, 0, received.length);

        os.close();
    }

}