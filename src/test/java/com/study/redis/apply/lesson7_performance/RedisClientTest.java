package com.study.redis.apply.lesson7_performance;

import com.study.redis.apply.RedisApplyApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

/**
 * @author Hash
 * @since 2020/12/20
 */
public class RedisClientTest extends RedisApplyApplicationTests {

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;

    @Test
    public void test() throws IOException {
        RedisClient jedis = new RedisClient(redisHost, redisPort);
        final String response = jedis.set("hello", "world!");
        System.out.println("response:" + response);
        System.out.println("设置完成####################");

        String value = jedis.get("hello");
        System.out.println(value);
    }
}
