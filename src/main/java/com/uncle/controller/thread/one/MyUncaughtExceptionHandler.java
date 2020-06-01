package com.uncle.controller.thread.one;

/**
 * @author 杨戬
 * @className MyUncaughtExceptionHandler
 * @email yangb@chaosource.com
 * @date 20-6-1 11:12
 */
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{
    /**
     * Method invoked when the given thread terminates due to the
     * given uncaught exception.
     * <p>Any exception thrown by this method will be ignored by the
     * Java Virtual Machine.
     *
     * @param t the thread
     * @param e the exception
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("抛出异常的线程名： " + t.getName());
        System.out.println("抛出的异常是： " + e);
    }
}
