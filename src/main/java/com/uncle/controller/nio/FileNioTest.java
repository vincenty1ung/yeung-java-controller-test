package com.uncle.controller.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 杨戬
 * @className FileNioTest
 * @email uncle.yeung.bo@gmail.com
 * @date 20-4-2 10:50
 */
public class FileNioTest {
    public static void main(String[] args) {
        while (true) {
            try {
                RandomAccessFile rdf = new RandomAccessFile("/home/uncle/tmp/hello.txt", "rw");
                //利用channel中的FileChannel来实现文件的读取
                FileChannel inChannel = rdf.getChannel();
                //设置缓冲区容量为10
                ByteBuffer buf = ByteBuffer.allocate(10);
                //从通道中读取数据到缓冲区，返回读取的字节数量
                int byteRead = inChannel.read(buf);
                //数量为-1表示读取完毕。
                while (byteRead != -1) {
                    //切换模式为读模式，其实就是把postion位置设置为0，可以从0开始读取
                    buf.flip();
                    //如果缓冲区还有数据
                    while (buf.hasRemaining()) {
                        //输出一个字符
                        System.out.print((char) buf.get());
                    }
                    //数据读完后清空缓冲区
                    buf.clear();
                    //继续把通道内剩余数据写入缓冲区
                    byteRead = inChannel.read(buf);
                }
                //关闭通道
                rdf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Thread.sleep(1000);
        }
    }
}
