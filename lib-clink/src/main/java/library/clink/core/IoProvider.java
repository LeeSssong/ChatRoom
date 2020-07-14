package src.main.java.library.clink.core;

import java.io.Closeable;
import java.nio.channels.SocketChannel;

public interface IoProvider extends Closeable {
    boolean registerInput(SocketChannel channel, HandleInputCallback callback);

    boolean registerOutput(SocketChannel channel, HandleOutputCallback callback);

    void unRegisterInput(SocketChannel channel);

    void unRegisterOutput(SocketChannel channel);

    abstract class HandleInputCallback implements Runnable {
        @Override
        public final void run() {
            canProviderInput();
        }

        protected abstract void canProviderInput();
    }

    abstract class HandleOutputCallback implements Runnable {
        private Object attach;

        @Override
        public final void run() {
            canProviderOutput(attach);
        }

        // 有了消息但尚未能进行发送（还没得到调度），则把消息先保存，等待发送
        public final void setAttach(Object attach) {
            this.attach = attach;
        }

        protected abstract void canProviderOutput(Object attach);
    }

}
