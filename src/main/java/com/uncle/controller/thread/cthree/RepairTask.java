package com.uncle.controller.thread.cthree;

/**
 * @author 杨戬
 * @className RepairTask
 * @email yangb@chaosource.com
 * @date 20-6-1 17:42
 */
public class RepairTask implements Runnable {

    private Washroom washroom;

    public RepairTask(Washroom washroom) {
        this.washroom = washroom;
    }

    @Override
    public void run() {
        synchronized (washroom.getLock()) {
            System.out.println("维修工 获取了厕所的锁");
            System.out.println("厕所维修中，维修厕所是一件辛苦活，需要很长时间。。。");

            try {
                // 用线程sleep表示维修的过程
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 维修结束把厕所置为可用状态
            washroom.setAvailable(true);
            washroom.getLock().notifyAll();
            System.out.println("维修工把厕所修好了，准备释放锁了");
        }
    }

}
