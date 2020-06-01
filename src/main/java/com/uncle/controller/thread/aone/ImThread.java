package com.uncle.controller.thread.aone;

/**
 * @author 杨戬
 * @className ExThread
 * @email yangb@chaosource.com
 * @date 20-6-1 10:52
 */
public class ImThread implements Runnable{

    @Override
    public void run() {

        System.out.println("true = " + true + Thread.currentThread().getId() + Thread.currentThread().getName()+Thread.currentThread().getPriority());
    }

}
