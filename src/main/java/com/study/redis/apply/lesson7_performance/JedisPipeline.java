package com.study.redis.apply.lesson7_performance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hash
 * @since 2020/12/20
 */
@Service
public class JedisPipeline {

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;

    /**
     * 用jedis实现的pipeline，有三个步骤。
     *
     * @param batchSize
     */
    public void pipeline(int batchSize) {
        Jedis jedis = new Jedis(redisHost, redisPort);
        Pipeline p = jedis.pipelined();
        List<Response<?>> list = new ArrayList<Response<?>>();
        long s = System.currentTimeMillis();
        for (int i = 0; i < batchSize; i++) {
            Response<?> r = p.get("pipeline" + i);
            list.add(r);
        }
        System.out.println("write cost:" + (System.currentTimeMillis() - s));
        p.sync();
        list.forEach((e) -> {
            System.out.println(e.get());
        });
        System.out.println("read cost:" + (System.currentTimeMillis() - s));
        p.close();
        jedis.close();
    }
}
