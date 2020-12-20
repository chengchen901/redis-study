package com.study.redis.apply.lesson8_practice;

import com.study.redis.apply.RedisApplyApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Hash
 * @since 2020/12/20
 */
public class SimpleRateLimiterTests extends RedisApplyApplicationTests {

    @Autowired
    SimpleLimiter limiter;

    @Test
    public void test() {
        System.out.println("测试简单限流，1分钟允许访问10次");
        for(int i = 0; i < 12; i++) {
            // 1分钟允许访问10次
            boolean result = limiter.isActionAllowed("user:hash", "page/limit", 1 * 60, 10);
            System.out.println((i+1)+"次，获取令牌："+result);
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
