package com.jojo.socket.mina;

import com.google.common.base.Charsets;
import com.jojo.socket.request.BaseRequest;
import com.jojo.socket.response.BaseResponse;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.locks.ReentrantLock;

public class MinaClient {

    private static final Logger logger = LoggerFactory.getLogger(MinaClient.class);

    private String host;
    private int port;

    private IoSession ioSession;

    private BaseResponse receivedResponse;

    private ReentrantLock sendLock = new ReentrantLock();

    private Object waitLock = new Object();

    private IoSession getSession() {
        if (this.isActive()) {
            return ioSession;
        }
        // 先做关闭
        this.close();

        //创建连接
        IoConnector connector = new NioSocketConnector();

        // 设置编解码器
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(
                Charsets.UTF_8, LineDelimiter.MAC, LineDelimiter.MAC)));

        // 设置请求以及出入参处理器
        connector.setHandler(new SimpleMinaHandler());

        // 连接
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(host, port));
        connectFuture.awaitUninterruptibly();// 等待连接成功

        // 获取session
        IoSession session = connectFuture.getSession();

        ioSession = session;
        return ioSession;
    }

    public boolean isActive() {
        return ioSession != null && ioSession.isConnected() && ioSession.isActive();
    }

    public void close() {
        if (ioSession != null) {
            IoService ioService = ioSession.getService();

            ioSession.closeNow();// 关闭session
            ioService.dispose(false);
        }
    }

    public BaseResponse writeAndWaitResponse(BaseRequest request) {
        try {
            sendLock.lock();
            receivedResponse = null;// 清空接收器

            IoSession session = getSession();
            session.write(request.pack());

            waitLock.wait(15 * 1000);

            if (receivedResponse == null) {
                return BaseResponse.fail("请求失败，未收到返回值");
            }

            return receivedResponse;

        } catch (Exception e) {
            logger.error("发送消息异常", e);
            return BaseResponse.fail("请求异常，未收到返回值" + e.getMessage());
        } finally {
            sendLock.unlock();
        }
    }

    protected BaseResponse unpack(Object message) {
        return null;
    }


    class SimpleMinaHandler extends IoHandlerAdapter {
        @Override
        public void sessionCreated(IoSession session) throws Exception {
            logger.info("创建");
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            logger.info("打开");
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            logger.info("关闭");
        }

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            logger.info("收到消息===" + message);

            receivedResponse = unpack(message);

            waitLock.notifyAll();
        }

        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            logger.info("发送消息===" + message);
        }
    }
}
