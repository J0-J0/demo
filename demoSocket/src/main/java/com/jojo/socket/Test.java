package com.jojo.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    private static final int BUFFER_LENGTH = 10 * 1024;


    public static void main(String[] args) throws Exception {
        String host = "aleph-01.clcn.net.cn";
        int port = 5331;
        String message = "1720210223    231011|AOST001SSLSW|AB001T200270647|AC";
        String charset = "UTF-8", endDelimiter = "\r";

        message += endDelimiter;

//        bioSocket(host, port, message, charset);
//        nioSocketBlocking(host, port, message, charset);
        nioSocket(host, port, message, charset);


    }

    /**
     * 客户端，发送请求
     * @param host
     * @param port
     * @param message
     * @param charset
     * @throws IOException
     */
    private static void nioSocket(String host, int port, String message, String charset) throws IOException {
        Selector selector = Selector.open();

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(host, port));

        int opKeys = SelectionKey.OP_CONNECT | SelectionKey.OP_READ;
        socketChannel.register(selector, opKeys);

        while (true) {
            int select = selector.select();
            if (select == 0) {
                break;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (Iterator<SelectionKey> iterator = selectionKeys.iterator(); iterator.hasNext(); ) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isConnectable()) {
                    logger.info("连接成功");

                    if (!socketChannel.isConnected()) {
                        socketChannel.finishConnect();
                    }

                    logger.info("nioSocket===request==={}", message);
                    ByteBuffer srcByteBuffer = ByteBuffer.wrap(message.getBytes());
                    socketChannel.write(srcByteBuffer);

                } else if (selectionKey.isReadable()) {
                    ByteBuffer destByteBuffer = ByteBuffer.allocate(BUFFER_LENGTH);
                    int readLength = socketChannel.read(destByteBuffer);
                    destByteBuffer.flip();// 重置position，开始读取

                    byte[] dst = new byte[BUFFER_LENGTH];
                    destByteBuffer.get(dst, 0, readLength);

                    logger.info("nioSocket===response==={}", new String(dst, 0, readLength, charset));
                }
                iterator.remove();
            }


        }
    }

    private static void nioSocketBlocking(String host, int port, String message, String charset) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(host, port));

        logger.info("nioSocket===request==={}", message);
        ByteBuffer srcByteBuffer = ByteBuffer.wrap(message.getBytes());
        socketChannel.write(srcByteBuffer);

        ByteBuffer destByteBuffer = ByteBuffer.allocate(BUFFER_LENGTH);
        int readLength = socketChannel.read(destByteBuffer);
        destByteBuffer.flip();// 重置position，开始读取

        byte[] dst = new byte[BUFFER_LENGTH];
        destByteBuffer.get(dst, 0, readLength);

        logger.info("nioSocket===response==={}", new String(dst, 0, readLength, charset));
    }

    private static void bioSocket(String host, int port, String message, String charset) throws IOException {

        Socket socket = new Socket(host, port);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(message.getBytes(charset));
        outputStream.flush();
        System.out.println("客户端发送：" + message);

        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[10240];
        StringBuilder sb = new StringBuilder();
        int len = inputStream.read(bytes);
        sb.append(new String(bytes, 0, len, charset));
        System.out.println("获取数据，长度：" + len + "，内容为：" + sb);
    }
}
