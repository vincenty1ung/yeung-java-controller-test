package com.uncle.controller.redisson;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 杨戬
 * @className RedissonManager
 * @email uncle.yeung.bo@gmail.com
 * @date 19-8-15 17:33
 */
@Configuration
public class RedissonManager {
    private static final String RAtomicName = "genId_";
    private static final String redis = "redis://";
    private Config config = new Config();
    private RedissonClient redisson = null;

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private String redisPort;

    @Bean
    public RedissonClient redissonClient() {
        init("yangbo", "15");
        return getRedisson();
    }
    @Bean
    public DistributedRedisLock distributedRedisLock(@Qualifier("redissonClient") RedissonClient redissonClient) {
        DistributedRedisLock distributedRedisLock = new DistributedRedisLock();
        distributedRedisLock.setRedissonClient(redissonClient);
        return distributedRedisLock;
    }


    ;

    void init(String key, String value) {
        try {
/*            config.useClusterServers() //这是用的集群server
                    .setScanInterval(2000) //设置集群状态扫描时间
                    .setMasterConnectionPoolSize(10000) //设置连接数
                    .setSlaveConnectionPoolSize(10000)
                    .addNodeAddress("127.0.0.1:6379");*/
            if (key == null || "".equals(key)) {
                key = RAtomicName;
            }
            config.useSingleServer().setAddress(redis + redisHost + ":" + redisPort);
            redisson = Redisson.create(config);
            //清空自增的ID数字
            RAtomicLong atomicLong = redisson.getAtomicLong(key);
            long pValue = 1;
            if (value != null && !"".equals(value)) {
                pValue = Long.parseLong(value);
            }
            atomicLong.set(pValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RedissonClient getRedisson() {
        return redisson;
    }

    /**
     * 获取redis中的原子ID
     */
    public Long nextID() {
        RAtomicLong atomicLong = getRedisson().getAtomicLong(RAtomicName);
        atomicLong.incrementAndGet();
        return atomicLong.get();
    }
}
