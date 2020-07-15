package src.main.java.library.clink.core;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class IoArgs {
    private int limit = 256;
    private byte[] byteBuffer = new byte[256];
    private ByteBuffer buffer = ByteBuffer.wrap(byteBuffer);

    /**
    * @Description: 从 bytes 中读取数据
    * @Param: [bytes, offset]
    * @return: int
    * @Author: LeeSongs
    * @Date: 2020/7/14
    */
    public int readFrom(byte[] bytes, int offset) {
        int size = Math.min(bytes.length - offset, buffer.remaining());
        buffer.put(bytes, offset, size);
        return size;
    }

    /**
    * @Description: 写入数据到 bytes 中
    * @Param: [bytes, offset]
    * @return: int
    * @Author: LeeSongs
    * @Date: 2020/7/14
    */
    public int writeTo(byte[] bytes, int offset) {
        int size = Math.min(bytes.length - offset, buffer.remaining());
        buffer.get(bytes, offset, size);
        return size;
    }

    /**
    * @Description: 从 SocketChannel 中读取数据
    * @Param: [channel]
    * @return: int
    * @Author: LeeSongs
    * @Date: 2020/7/14
    */
    public int readFrom(SocketChannel channel) throws IOException {
        startWriting();

        int bytesProduced = 0;
        while (buffer.hasRemaining()) {
            int len = channel.read(buffer);
            if (len < 0) {
                throw new EOFException();
            }
            bytesProduced += len;
        }

        finishWriting();
        return bytesProduced;
    }


    /**
    * @Description: 写数据到 SocketChannel
    * @Param: [channel]
    * @return: int
    * @Author: LeeSongs
    * @Date: 2020/7/14
    */
    public int writeTo(SocketChannel channel) throws IOException {
        int bytesProduced = 0;
        while (buffer.hasRemaining()) {
            int len = channel.write(buffer);
            if (len < 0) {
                throw new EOFException();
            }
            bytesProduced += len;
        }
        return bytesProduced;
    }

    /**
    * @Description: 开始写入数据到 IoArgs
    * @Param: []
    * @return: void
    * @Author: LeeSongs
    * @Date: 2020/7/14
    */
    public void startWriting(){
        buffer.clear();
        // 定义容纳区间
        buffer.limit(limit);
    }

    /**
    * @Description: 写完数据后调用
    * @Param: []
    * @return: void
    * @Author: LeeSongs
    * @Date: 2020/7/14
    */
    public void finishWriting(){
        buffer.flip();
    }

    /**
    * @Description: 单次写操作的容纳区间
    * @Param: [limit]
    * @return: void
    * @Author: LeeSongs
    * @Date: 2020/7/14
    */
    public void limit(int limit) {
        this.limit = limit;
    }

    public void writeLength(int total) {
        buffer.putInt(total);
    }

    public int readLength() {
        return buffer.getInt();
    }

    public int capacity() {
        return buffer.capacity();
    }

    public interface IoArgsEventListener {
        void onStarted(IoArgs args);

        void onCompleted(IoArgs args);
    }
}
