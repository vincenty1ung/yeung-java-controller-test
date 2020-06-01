package com.uncle.controller.thread.one;

import lombok.SneakyThrows;

/**
 * @author 杨戬
 * @className ExThread
 * @email yangb@chaosource.com
 * @date 20-6-1 10:52
 */
public class ExThread extends Thread {

    @SneakyThrows
    @Override
    public void run() {
        // sleep方法是一个静态方法，它会让当前线程暂停指定的时间。
        // 这个所谓的暂停，或者说休眠其实只是把正在运行的线程阻塞掉，放到阻塞队列里，等指定的时间一到，再从阻塞队列里出来而已
        // ～另外当前方法native修饰 为底层java调用动态库(c++实现)实现睡眠
        Thread.sleep(1);
        System.out.println("true = " + true + Thread.currentThread().getId() + Thread.currentThread().getName()
            + Thread.currentThread().getPriority());
    }

}
