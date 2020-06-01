package com.uncle.controller.thread.cthree;

/**
 * @author 杨戬
 * @className Washroom
 * @email yangb@chaosource.com
 * @date 20-6-1 17:42
 */
public class Washroom {
    // 表示厕所是否是可用的状态
    private volatile boolean isAvailable = false;

    // 厕所门的锁
    private Object lock = new Object();

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public Object getLock() {
        return lock;
    }
}
