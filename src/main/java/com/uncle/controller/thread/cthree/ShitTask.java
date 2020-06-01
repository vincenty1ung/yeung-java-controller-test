package com.uncle.controller.thread.cthree;

import lombok.SneakyThrows;

/**
 * @author 杨戬
 * @className ShitTask
 * @email yangb@chaosource.com
 * @date 20-6-1 17:43
 */
public class ShitTask  implements Runnable {
    private Washroom washroom;

    private String name;

    public ShitTask(Washroom washroom, String name) {
        this.washroom = washroom;
        this.name = name;
    }

    @SneakyThrows
    @Override
    public void run() {
        synchronized (washroom.getLock()) {
            System.out.println(name + " 获取了厕所的锁");
            while (!washroom.isAvailable()) {
                // 不能上厕所将锁置为等待,并加入等待队列
                washroom.getLock().wait();
            }
            System.out.println(name + " 上完了厕所");
        }
    }

}
