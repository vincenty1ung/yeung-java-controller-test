package com.uncle.controller.thread.dfour;

import java.util.Random;

/**
 * @author 杨戬
 * @className SleepUtil
 * @email yangb@chaosource.com
 * @date 20-6-1 21:26
 */
public class SleepUtil {
    private static Random random = new Random();

    public static void randomSleep() {
        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
