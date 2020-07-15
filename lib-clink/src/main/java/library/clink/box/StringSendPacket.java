package src.main.java.library.clink.box;

import src.main.java.library.clink.core.SendPacket;

import java.io.IOError;
import java.io.IOException;

/**
 * 字符串发送包
 */
public class StringSendPacket extends SendPacket {

    private final byte[] bytes;

    public StringSendPacket(String msg) {
        this.bytes = msg.getBytes();
        this.length = bytes.length;
    }

    @Override
    public byte[] bytes() {
        return new byte[0];
    }

    @Override
    public void close() throws IOException {}
}
