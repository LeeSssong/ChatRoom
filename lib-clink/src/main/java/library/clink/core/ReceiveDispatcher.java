package src.main.java.library.clink.core;

import java.io.Closeable;

/**
 * @Desc: 接收的数据调度封装；把一份或者多份 IoArgs 组合成一份 Packet
 * @authonr: LeeSongsheng
 * @create: 2020/07/14
 **/
public interface ReceiveDispatcher extends Closeable {
    void start();

    void sto();

    interface ReceivePacketCallback {
        void onReceivePacketCompleted(ReceivePacket packet);
    }
}
