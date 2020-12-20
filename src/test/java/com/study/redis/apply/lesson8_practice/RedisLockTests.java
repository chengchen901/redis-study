package com.study.redis.apply.lesson8_practice;

import com.study.redis.apply.RedisApplyApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Hash
 * @since 2020/12/20
 */
public class RedisLockTests extends RedisApplyApplicationTests {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() {
        RedisDistributedLock lock = new RedisDistributedLock("payLock001", 30, stringRedisTemplate);
        lock.lock();
        try {
            // 执行业务
            Thread.sleep(3000L);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
