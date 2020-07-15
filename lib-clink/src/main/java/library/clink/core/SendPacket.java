package src.main.java.library.clink.core;

/**
 * @Desc: 发送包定义
 * @authonr: LeeSongsheng
 * @create: 2020/07/14
 **/
public abstract class SendPacket extends Packet{
    // 发送的包是否已取消
    private boolean isCanceled;

    // 发送的内容
    public abstract byte[] bytes();

    public boolean isCanceled() {
        return isCanceled;
    }
}
