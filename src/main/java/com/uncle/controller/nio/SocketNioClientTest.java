package com.uncle.controller.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author 杨戬
 * @className SocketNioClientTest
 * @email uncle.yeung.bo@gmail.com
 * @date 20-4-2 10:51
 */
public class SocketNioClientTest {
    public static void main(String[] args) {
        try {
            while (true) {
                SocketChannel socketChannel = SocketChannel.open();
                //打开socket连接，连接本地8090端口，也就是服务端
                socketChannel.connect(new InetSocketAddress("127.0.0.1", 8090));
                //请求服务端，发送请求
                ByteBuffer buf1 = ByteBuffer.allocate(1024);
                ByteBuffer header = ByteBuffer.allocate(6);
                ByteBuffer body = ByteBuffer.allocate(1024);
                ByteBuffer[] bufferArray = {header, body};


                buf1.put("来着客户端的请求".getBytes());
                buf1.flip();
                if (buf1.hasRemaining()) {
                    socketChannel.write(buf1);
                }
                buf1.clear();
                //接受服务端的返回，构造接受缓冲区，我们定义头6个字节为头部，后续其他字节为主体内容。

                socketChannel.read(bufferArray);
                header.flip();
                body.flip();
                if (header.hasRemaining()) {
                    System.out.println(">>>客户端接收头部数据：" + new String(header.array()));
                }
                if (body.hasRemaining()) {
                    System.out.println(">>>客户端接收body数据：" + new String(body.array()));
                }
                header.clear();
                body.clear();
                socketChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
