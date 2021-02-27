
package com.jojo.zzz;

import com.google.common.base.Charsets;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Work {
    private static final Logger logger = LoggerFactory.getLogger(Work.class);


    static class SimpleMinaHandler extends IoHandlerAdapter {
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
        }

        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            logger.info("发送消息===" + message);
        }
    }

    private static void connectByMina(String host, int port, String message) throws InterruptedException {
        //创建连接
        IoConnector connector = new NioSocketConnector();

        // 设置编解码器
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charsets.UTF_8, LineDelimiter.MAC, LineDelimiter.MAC)));

        // 设置请求以及出入参处理器
        connector.setHandler(new SimpleMinaHandler());

        // 连接
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(host, port));
        connectFuture.awaitUninterruptibly();// 等待连接成功

        // 获取session
        IoSession session = connectFuture.getSession();

        // 发送消息
        logger.info("准备发送消息，当前线程{}", Thread.currentThread().getName());
        session.write(message);

        // 连接关闭
        Thread.sleep(1 * 1000);
        connector.dispose();
    }


    static class SimpleNettyInitializer extends ChannelInitializer<SocketChannel> {
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            //(1) 加入拆包器
            pipeline.addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE));
            //(2) 加入粘包器
//            pipeline.addLast(new LengthFieldPrepender(4));
            //字符串解码 (3)
            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
            //字符串编码 (4)
            pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));

            pipeline.addLast(new SimpleNettyHandler());
        }
    }

    static class SimpleNettyHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            logger.info("注册");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            logger.info("激活");
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            logger.info("读取完毕");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            logger.info("收到消息==={}", msg);
        }
    }


    public static void main(String[] args) throws Exception {

        String host = "aleph-01.clcn.net.cn";
        int port = 5331;
        String message = "1720210223    231011|AOST001SSLSW|AB001T200270647|AC";

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new SimpleNettyInitializer());

        ChannelFuture channelFuture = bootstrap.connect(host, port).sync();//等待连接成功

        channelFuture.channel().writeAndFlush(message + "/r");

    }


}