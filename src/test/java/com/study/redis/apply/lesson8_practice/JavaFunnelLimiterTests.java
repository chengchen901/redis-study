package com.study.redis.apply.lesson8_practice;

import com.study.redis.apply.RedisApplyApplicationTests;
import org.junit.jupiter.api.Test;

/**
 * @author Hash
 * @since 2020/12/20
 */
public class JavaFunnelLimiterTests extends RedisApplyApplicationTests {

    @Test
    public void testFunnel() {
        System.out.println("测试漏牌桶算法");
        JavaFunnelLimiter limiter = new JavaFunnelLimiter();
        for(int i = 0; i < 10; i++) {
            // 1s操作一次, 0.001f
            boolean result = limiter.isActionAllowed("user01", "page/limit", 5, 0.001f);
            System.out.println((i+1)+"次，获取令牌："+result);
            try {
                Thread.sleep(300L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
