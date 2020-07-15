package src.main.java.library.clink.impl.async;

import src.main.java.library.clink.box.StringReceivePacket;
import src.main.java.library.clink.core.*;
import src.main.java.library.clink.utils.CloseUtils;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Desc:
 * @authonr: LeeSongsheng
 * @create: 2020/07/15
 **/
public class AsyncReceiveDispatcher implements ReceiveDispatcher {

    private final AtomicBoolean isClosed = new AtomicBoolean(false);

    private final Receiver receiver;
    private final ReceivePacketCallback callback;

    private IoArgs ioArgs = new IoArgs();
    private ReceivePacket packetTemp;
    private int total;
    private int position;
    private byte[] buffer;

    public AsyncReceiveDispatcher(Receiver receiver, ReceivePacketCallback callback) throws IOException {
        this.receiver = receiver;
        this.callback = callback;
        this.receiver.setReceiveListener(ioArgsEventListener);
    }


    /**
     * 开始进入接收方法
     */
    @Override
    public void start() {
        registerReceive();
    }
    /**
     * 注册接收数据
     */
    private void registerReceive() {
        try {
            receiver.receiveAsync(ioArgs);
        } catch (IOException e) {
            closeAndNotify();
        }
    }
    /**
     * 自主发起的关闭操作，并且需要进行通知
     */
    private void closeAndNotify() {
        CloseUtils.close(this);
    }

    @Override
    public void sto() {

    }

    @Override
    public void close() throws IOException {
        if (isClosed.compareAndSet(false, true)) {
            ReceivePacket packet = packetTemp;
            if (packet != null) {
                packetTemp = null;
                CloseUtils.close(packet);
            }
        }
    }

    private final IoArgs.IoArgsEventListener ioArgsEventListener = new IoArgs.IoArgsEventListener() {
        @Override
        public void onStarted(IoArgs args) {
            int receiveSize;
            if (packetTemp == null)
                receiveSize = 4;
            else {
                receiveSize = Math.min(total - position, args.capacity());
            }
            // 设置本次接收数据大小
            args.limit(receiveSize);
        }

        @Override
        public void onCompleted(IoArgs args) {
            // 解析数据
            assemblePacket(args);
            // 继续接收下一条数据
            registerReceive();
        }


    };
    /**
     * @Description: 解析数据到 Packet
     * @Param: [args]
     * @return: void
     * @Author: LeeSongs
     * @Date: 2020/7/15
     */
    private void assemblePacket(IoArgs args) {
        if (packetTemp == null) {
            int length = args.readLength();
            packetTemp = new StringReceivePacket(length);
            buffer = new byte[length];
            total = length;
            position = 0;
        }

        int count = args.writeTo(buffer, 0);
        if (count > 0) {
            packetTemp.save(buffer, count);
            position += count;

            // 检查是否已完成
            if (position == total) {
                completePacket();
                packetTemp = null;
            }
        }
    }

    /**
    * @Description: 完成数据接收
    * @Param: []
    * @return: void
    * @Author: LeeSongs
    * @Date: 2020/7/15
    */
    private void completePacket() {
        ReceivePacket packet = this.packetTemp;
        CloseUtils.close(packet);
        callback.onReceivePacketCompleted(packet);
    }
}
