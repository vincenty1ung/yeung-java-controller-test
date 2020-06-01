package com.uncle.controller.thread.aone;

import com.uncle.core.SysCode;
import com.uncle.core.UncleException;

/**
 * @author 杨戬
 * @className Test
 * @email yangb@chaosource.com
 * @date 20-6-1 10:53
 */
public class Test {
    public static void main(String[] args) {
        /*
        1.
        线程可以被分成两种类型，一种叫普通线程，另一种就叫守护线程，守护线程也被称为后台线程。
        守护线程在程序执行过程中并不是不可或缺的，它主要是为普通线程提供便利服务的,比方说 java 中不需要我们手动去释放某个对象的内存，
        它有一种传说中的垃圾收集器在不停的去释放程序中不需要的内存，在我们启动程序的时候系统会帮助我们创建一个负责收集垃圾的线程，
        这个线程就是一个守护线程。如果所有的普通线程全部死掉了，那守护线程也会跟着死掉，也就是程序就退出了，比方说在所有的普通线程停止了之后，
        这个负责收集垃圾的守护线程也会自动退出的。反过来说只要有一个普通线程活着，程序就不会退出。
        我们可以通过下边这些方法来判断一个线程是否是守护线程或者把一个线程设置为守护线程：
        * */
        System.out.println("main线程是否是守护线程：" + Thread.currentThread().isDaemon());

        ExThread thread1 = new ExThread();
        thread1.setName("thread1");
        thread1.setPriority(Thread.MIN_PRIORITY);
        thread1.start();
        System.out.println("thread1线程是否是守护线程：" + thread1.isDaemon());

        Thread thread2 = new Thread(new ImThread());
        thread2.setName("thread2");
        thread2.setPriority(Thread.MAX_PRIORITY);
        System.out.println("thread2线程是否是守护线程：" + thread2.isDaemon());
        thread2.setDaemon(true);
        System.out.println("thread2线程是否是守护线程：" + thread2.isDaemon());
        thread2.start();

        /*
        2.
        有的时候需要等待一个线程执行完了，另一个线程才能继续执行，Thread类提供了这样的方法：
        
        void join()
        
        等待该线程终止才能继续执行。
        
        void join(long millis)
        
        在指定毫秒数内等待该线程终止，如果到达了指定时间就继续之行了。
        
        void join(long millis, int nanos)
        
        跟上边方法一个意思，只不过加了个纳秒限制。
        * */
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println(i);
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "t1");

        t1.start();

        Thread t2 = new Thread(new Runnable() {
            double d = 1.2d;

            @Override
            public void run() {
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("线程t1执行完了，该t2了");
            }
        });

        t2.start();

        /*
        线程异常处理
        * */
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                throw new UncleException(SysCode.ERRORS);
            }
        }, "t3");
        t3.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        t3.start();

    }
    /*
        总结
           当我们运行一个 java 程序时，系统会为我们默认创建一个名叫main的线程。
    
           java 中的任务被抽象成了一个Runnable接口，我们的自定义任务需要去实现这个接口，并把任务的详细内容写在覆盖的 run 方法里。
    
           java 中的Thread类来代表一个线程，它提供设置线程名、线程要执行的任务等相关构造方法。
    
           调用Thread对象的start方法可以执行一个线程，使用某个线程执行某个任务的方式有两种：
    
                创建 Thread 对象的时候指定需要执行的任务。
    
                通过继承 Thread 类并覆盖 run 方法。
    
           Thread类提供一系列方法来方便我们获取线程的信息或者控制线程，包括下边这些功能：
    
                获取线程 ID
    
                获取和设置线程名称
    
                获取和设置线程优先级
    
                线程休眠
    
                获取当前正在执行的线程
    
                守护线程的设置和判断相关方法
    
                让出本次处理器时间片
    
                等待另一线程执行结束
    
           可以为某个线程设置异常处理器来捕获线程中没有catch的异常。
    * */
}
