package com.uncle.controller.redisson;

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 杨戬
 * @className TestController
 * @email uncle.yeung.bo@gmail.com
 * @date 19-8-16 15:45
 */
@RestController
public class TestRedissonController {
    @Resource
    private DistributedRedisLock distributedRedisLock;

    @RequestMapping("/test/{pathValue}")
    public void get(@PathVariable("pathValue") String pathValue){

        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Mutex acquire = null;
                    try {
                        acquire = distributedRedisLock.acquire(pathValue);
                        if (acquire != null) {
                            try {
                                Thread.sleep(5000); //获得锁之后可以进行相应的处理
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.err.println("======获得锁后进行相应的操作======");
                        } else {
                            System.err.println("获取锁失败");
                            throw new RuntimeException("获取锁失败-订单重复");
                        }

                    } finally {
                        if (acquire != null) {
                            //DistributedRedisLock.release(key);
                            System.err.println("==============解锁===============");
                        }
                    }
                }
            });
            t.start();
        }
    }

}
