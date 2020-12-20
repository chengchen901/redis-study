package com.study.redis.apply.lesson7_performance;

import com.study.redis.apply.RedisApplyApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Hash
 * @since 2020/12/20
 */
public class PipelineTests extends RedisApplyApplicationTests {

    @Autowired
    RedisPipeline redisPipeline;
    @Autowired
    JedisPipeline jedisP;

    @Test
    public void setTest() {
        int batchSize = 1000;
        long s = System.currentTimeMillis();
        redisPipeline.setCommand(batchSize);
        long t1 = (System.currentTimeMillis() - s);

        s = System.currentTimeMillis();
        redisPipeline.pipeline(batchSize);
        System.out.println("普通set耗时："+t1+"ms， pipeline耗时："+(System.currentTimeMillis() - s)+"ms");
    }

    @Test
    public void testJedisPipeline() {
        jedisP.pipeline(1000);
    }
}
