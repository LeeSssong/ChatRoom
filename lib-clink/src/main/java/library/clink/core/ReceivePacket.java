package src.main.java.library.clink.core;

/**
 * @Desc: 接受包的定义
 * @authonr: LeeSongsheng
 * @create: 2020/07/14
 **/
public abstract class ReceivePacket extends Packet{
    public abstract void save(byte[] bytes, int count);
}
