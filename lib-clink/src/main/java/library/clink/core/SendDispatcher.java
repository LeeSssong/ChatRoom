package src.main.java.library.clink.core;

import java.io.Closeable;

/**
 * @Desc: 发送数据调度者；缓存所有需要发送的数据，通过队列对数据进行发送；在发送数据时，实现对数据的基本包装。
 * @authonr: LeeSongsheng
 * @create: 2020/07/14
 **/
public interface SendDispatcher extends Closeable {
    // 发送一份数据
    void send(SendPacket packet);

    // 取消发送数据
    void cancel(SendPacket packet);
}
