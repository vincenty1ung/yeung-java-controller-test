package com.uncle.controller.thread.dfour;

import java.util.Queue;

/**
 * @author 杨戬
 * @className Cook
 * @email yangb@chaosource.com
 * @date 20-6-1 21:26
 */
public class Cook extends Thread {

    private Queue<Food> queue;

    public Cook(Queue<Food> queue, String name) {
        super(name);
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            // 模拟厨师炒菜时间
            SleepUtil.randomSleep();
            Food food = new Food();
            System.out.println(getName() + " 生产了" + food);
            synchronized (queue) {
                while (queue.size() > 4) {
                    try {
                        System.out.println("队列元素超过5个，为：" + queue.size() + " " + getName() + "抽根烟等待中");
                        queue.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                queue.add(food);
                queue.notifyAll();
            }
        }
    }
}
