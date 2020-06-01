package com.uncle.controller.thread.btwo;

/**
 * @author 杨戬
 * @className Increment
 * @email yangb@chaosource.com
 * @date 20-6-1 11:35
 */
public class IncrementThreadLocal {
    private ThreadLocal<Integer> local;

    public void increase() {
        local.set(local.get() + 1);
        System.out.println("local.get() = " + local.get());
    }

    public int getI() {
        return local.get();
    }

    public IncrementThreadLocal() {
        this.local = new ThreadLocal<Integer>() {
            @Override
            protected Integer initialValue() {
                return 0;
            }
        };

    }

    public static void test(int threadNum, int loopTimes) {
        IncrementThreadLocal increment = new IncrementThreadLocal();

        Thread[] threads = new Thread[threadNum];

        for (int i = 0; i < threads.length; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < loopTimes; i++) {
                        increment.increase();
                    }
                }
            });
            threads[i] = t;
            t.start();
        }

        for (Thread t : threads) { // main线程等待其他线程都执行完成
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(threadNum + "个线程，循环" + loopTimes + "次结果：" + increment.getI());
    }

    public static void main(String[] args) {
        test(20, 1);
        test(20, 10);
        test(20, 100);
        test(20, 1000);
        test(20, 10000);
        test(20, 100000);
    }
}
