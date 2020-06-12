package com.uncle.controller.thread.dfour;

/**
 * @author 杨戬
 * @className Food
 * @email yangb@chaosource.com
 * @date 20-6-1 21:25
 */
public class Food {
    private static int counter = 0;

    private int i;  //代表生产的第几个菜

    public Food() {
        i = ++counter;
    }

    @Override
    public String toString() {
        return "第" + i + "个菜";
    }
}
