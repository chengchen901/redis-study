package com.study.redis.apply.lesson6_expired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 互斥锁：JVM锁 or 分布式锁
 *
 * @author Hash
 * @since 2020/12/20
 */
@Component
public class GoodsService2 {

    private final Logger logger = LoggerFactory.getLogger(GoodsService2.class);

    @Resource(name = "mainStringRedisTemplate")
    StringRedisTemplate mainRedisTemplate;

    @Autowired
    DatabaseService databaseService;

    Lock lock = new ReentrantLock();

    /**
     * 查询商品库存数
     *
     * @param goodsId 商品ID
     * @return 商品库存数
     */
    // @Cacheable 不管用什么样的方式，核心步骤 1，2，3
    public Object queryStock(final String goodsId) throws InterruptedException {
        // 1. 先从redis缓存中获取余票信息
        String cacheKey = "goodsStock-"+goodsId;
        String value = mainRedisTemplate.opsForValue().get(cacheKey);
        if (value != null) {
            logger.warn(Thread.currentThread().getName() + "缓存中取得数据==============>" + value);
            return value;
        }

        // 2000 请求
        // 同步 一个个来
        lock.lock(); // 2000 线程 1个线程拿到，1999 等待排队
        try {
            // 再次获取缓存
            value = mainRedisTemplate.opsForValue().get(cacheKey);
            if (value != null) {
                logger.warn(Thread.currentThread().getName() + "缓存中取得数据==============>" + value);
                return value;
            }

            // 拿到锁 重建缓存
            // 2. 缓存中没有,则取数据库
            value = databaseService.queryFromDatabase(goodsId);
            System.out.println(Thread.currentThread().getName() + "从数据库中取得数据==============>" + value);

            // 3. 塞到缓存,120秒过期时间
            final String v = value;
            mainRedisTemplate.execute((RedisCallback<Boolean>) connection -> {
                return connection.setEx(cacheKey.getBytes(), 120, v.getBytes());
            });
        } finally {
            lock.unlock();
        }

        return value;
    }
}
