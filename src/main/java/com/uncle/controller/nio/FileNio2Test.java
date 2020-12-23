package com.uncle.controller.nio;

import lombok.SneakyThrows;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @author 杨戬
 * @className UnitTest1
 * @email yangb@chaosource.com
 * @date 2020/12/23 16:29
 */
public class FileNio2Test {
    private static final String prefix = "E:\\tmp\\";
    private static final int num = 6;

    public static void main(String[] args) throws Exception {
       /* streamCopy("input", "output1");
        bufferCopy("input", "output2");
        directBufferCopy("input", "output3");
        mappedByteBufferCopy("input", "output4");
        mappedByteBufferCopyByPart("input", "output5");
        channelCopy("input", "output6");*/
        //streamConcurrentlyCopy6();
        //streamCopy6();
        bufferConcurrentlyCopy6();
        //bufferCopy6();

    }

    private static void streamCopy6() throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        streamCopy("output1", "output1" + "bak");
        streamCopy("output2", "output2" + "bak");
        streamCopy("output3", "output3" + "bak");
        streamCopy("output4", "output4" + "bak");
        streamCopy("output5", "output5" + "bak");
        streamCopy("output6", "output6" + "bak");
        long endTime = System.currentTimeMillis();
        System.out.println("streamCopy6 cost:" + (endTime - startTime));
    }

    private static void streamConcurrentlyCopy6() throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        final CountDownLatch launchLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            int finalI = i;
            Runnable runnable = new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    int i1 = finalI + 1;
                    streamCopy("output" + i1, "output" + i1 + "bak");
                    launchLatch.countDown();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }

        launchLatch.await();
        long endTime = System.currentTimeMillis();
        System.out.println("streamCopy6 cost:" + (endTime - startTime));
    }

    private static void bufferCopy6() throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        bufferCopy("output1", "output1" + "bak");
        bufferCopy("output2", "output2" + "bak");
        bufferCopy("output3", "output3" + "bak");
        bufferCopy("output4", "output4" + "bak");
        bufferCopy("output5", "output5" + "bak");
        bufferCopy("output6", "output6" + "bak");
        long endTime = System.currentTimeMillis();
        System.out.println("bufferCopy6 cost:" + (endTime - startTime));
    }

    private static void bufferConcurrentlyCopy6() throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        final CountDownLatch launchLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            int finalI = i;
            Runnable runnable = new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    int i1 = finalI + 1;
                    bufferCopy("output" + i1, "output" + i1 + "bak");
                    launchLatch.countDown();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
        launchLatch.await();
        long endTime = System.currentTimeMillis();
        System.out.println("bufferCopy6 cost:" + (endTime - startTime));
    }

    private static void directBufferConcurrentlyCopy6() throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        final CountDownLatch launchLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            int finalI = i;
            Runnable runnable = new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    int i1 = finalI + 1;
                    directBufferCopy("input", "output" + i1);
                    launchLatch.countDown();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
        launchLatch.await();
        long endTime = System.currentTimeMillis();
        System.out.println("directBufferCopy6 cost:" + (endTime - startTime));
    }

    private static void channelConcurrentlyCopy6() throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        final CountDownLatch launchLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            int finalI = i;
            Runnable runnable = new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    int i1 = finalI + 1;
                    channelCopy("input", "output" + i1);
                    launchLatch.countDown();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
        launchLatch.await();
        long endTime = System.currentTimeMillis();
        System.out.println("channelCopy6 cost:" + (endTime - startTime));
    }

    /**
     * 使用stream
     */
    private static void streamCopy(String from, String to) throws IOException {
        long startTime = System.currentTimeMillis();

        File inputFile = new File(prefix + from);
        File outputFile = new File(prefix + to);

        FileInputStream fis = new FileInputStream(inputFile);
        FileOutputStream fos = new FileOutputStream(outputFile);

        byte[] bytes = new byte[1024];
        int len;
        while ((len = fis.read(bytes)) != -1) {
            fos.write(bytes, 0, len);
        }
        fos.flush();

        fis.close();
        fos.close();

        long endTime = System.currentTimeMillis();
        System.out.println("streamCopy cost:" + (endTime - startTime));
    }

    /**
     * 使用buffer
     */
    private static void bufferCopy(String from, String to) throws IOException {
        long startTime = System.currentTimeMillis();

        RandomAccessFile inputFile = new RandomAccessFile(prefix + from, "r");
        RandomAccessFile outputFile = new RandomAccessFile(prefix + to, "rw");

        FileChannel inputChannel = inputFile.getChannel();
        FileChannel outputChannel = outputFile.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (inputChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            outputChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        inputChannel.close();
        outputChannel.close();

        long endTime = System.currentTimeMillis();
        System.out.println("bufferCopy cost:" + (endTime - startTime));
    }

    /**
     * 使用堆外内存
     */
    private static void directBufferCopy(String from, String to) throws IOException {
        long startTime = System.currentTimeMillis();

        RandomAccessFile inputFile = new RandomAccessFile(prefix + from, "r");
        RandomAccessFile outputFile = new RandomAccessFile(prefix + to, "rw");

        FileChannel inputChannel = inputFile.getChannel();
        FileChannel outputChannel = outputFile.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        while (inputChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            outputChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        inputChannel.close();
        outputChannel.close();

        long endTime = System.currentTimeMillis();
        System.out.println("directBufferCopy cost:" + (endTime - startTime));
    }

    /**
     * 内存映射全量
     */
    private static void mappedByteBufferCopy(String from, String to) throws IOException {
        long startTime = System.currentTimeMillis();

        RandomAccessFile inputFile = new RandomAccessFile(prefix + from, "r");
        RandomAccessFile outputFile = new RandomAccessFile(prefix + to, "rw");

        FileChannel inputChannel = inputFile.getChannel();
        FileChannel outputChannel = outputFile.getChannel();

        MappedByteBuffer iBuffer = inputChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputFile.length());
        MappedByteBuffer oBuffer = outputChannel.map(FileChannel.MapMode.READ_WRITE, 0, inputFile.length());

        // 直接操作buffer，没有其他IO操作
        oBuffer.put(iBuffer);

        inputChannel.close();
        outputChannel.close();

        long endTime = System.currentTimeMillis();
        System.out.println("mappedByteBufferCopy cost:" + (endTime - startTime));
    }

    /**
     * 内存映射部分
     */
    private static void mappedByteBufferCopyByPart(String from, String to) throws IOException {
        long startTime = System.currentTimeMillis();

        RandomAccessFile inputFile = new RandomAccessFile(prefix + from, "r");
        RandomAccessFile outputFile = new RandomAccessFile(prefix + to, "rw");

        FileChannel inputChannel = inputFile.getChannel();
        FileChannel outputChannel = outputFile.getChannel();

        for (long i = 0; i < inputFile.length(); i += 1024) {
            long size = 1024;
            // 避免文件产生间隙
            if (i + size > inputFile.length()) {
                size = inputFile.length() - i;
            }
            MappedByteBuffer iBuffer = inputChannel.map(FileChannel.MapMode.READ_ONLY, i, size);
            MappedByteBuffer oBuffer = outputChannel.map(FileChannel.MapMode.READ_WRITE, i, size);
            oBuffer.put(iBuffer);
        }

        inputChannel.close();
        outputChannel.close();

        long endTime = System.currentTimeMillis();
        System.out.println("mappedByteBufferCopyByPart cost:" + (endTime - startTime));
    }

    /**
     * zero copy
     */
    private static void channelCopy(String from, String to) throws IOException {
        long startTime = System.currentTimeMillis();

        RandomAccessFile inputFile = new RandomAccessFile(prefix + from, "r");
        RandomAccessFile outputFile = new RandomAccessFile(prefix + to, "rw");

        FileChannel inputChannel = inputFile.getChannel();
        FileChannel outputChannel = outputFile.getChannel();

        inputChannel.transferTo(0, inputFile.length(), outputChannel);

        inputChannel.close();
        outputChannel.close();

        long endTime = System.currentTimeMillis();
        System.out.println("channelCopy cost:" + (endTime - startTime));
    }

}
