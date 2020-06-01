package com.uncle.controller.thread.cthree;

/**
 * @author 杨戬
 * @className Test
 * @email yangb@chaosource.com
 * @date 20-6-1 17:45
 */
public class Test {
    public static void main(String[] args) {
        Washroom washroom = new Washroom();
        new Thread(new ShitTask(washroom, "狗哥"), "BROTHER-DOG-THREAD").start();
        new Thread(new ShitTask(washroom, "猫爷"), "GRANDPA-CAT-THREAD").start();
        new Thread(new ShitTask(washroom, "王尼妹"), "WANG-NI-MEI-THREAD").start();


        new Thread(new RepairTask(washroom), "REPAIR-THREAD").start();

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
