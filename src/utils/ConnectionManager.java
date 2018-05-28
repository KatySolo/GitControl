package utils;

import javafx.util.Pair;

import java.io.*;
import java.net.Socket;

public class ConnectionManager {

    public static Pair<ICommandPacket, byte[]> decode(Socket s) throws IOException{
        while (true) {
            DataInputStream inputStream = new DataInputStream(s.getInputStream());
            int length = inputStream.readInt();
            if (length > 0) {
                byte[] message = new byte[length];
                inputStream.readFully(message);
                ICommandPacket packet = (ICommandPacket) Serializator.deserializeClass(message);
                if (packet.getDataLength() != 0) {
                    byte[] data = new byte[packet.getDataLength()];
                    inputStream.readFully(data);
                    return new Pair<>(packet, data);
                } else return new Pair<>(packet, null);
            }
        }
    }


    public static void send(Socket s,Pair<ICommandPacket, byte[]> output) throws IOException{
        DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());
        byte[] data = Serializator.serializeClass(output.getKey());
        if (data != null) {
            outputStream.writeInt(data.length);
            outputStream.write(data);
            if (output.getValue() != null)
                outputStream.write(output.getValue());
        } else throw new NullPointerException();
    }

}
