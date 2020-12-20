package com.study.redis.apply.lesson8_practice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import javax.annotation.PostConstruct;

/**
 * SimpleRateLimiter 简单版本的限流
 *
 * @author Hash
 * @since 2020/12/20
 */
@Component
public class SimpleLimiter {

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;

    private Jedis jedis;

    @PostConstruct
    public void init() {
        jedis = new Jedis(redisHost, redisPort);
    }

    // 判断当前动作是否超过了限制
    /**
     * 判断是否超出限制
     * @param userId 操作用户
     * @param actionKey 操作内容
     * @param period 时间窗口，单位秒
     * @param maxCount 允许的最大次数
     * @return 是否超出限制
     */
    public boolean isActionAllowed(String userId, String actionKey,
                                   int period, int maxCount) {
        String key = String.format("hist::%s::%s", userId, actionKey);
        long nowTs = System.currentTimeMillis();

        Pipeline pipe = jedis.pipelined();
        pipe.multi();	// 等待多种操作一起执行
        // 往zset中添加记录，当前时间作为score，value只要唯一就好
        pipe.zadd(key, nowTs, "" + nowTs);
        // 根据score排序，删除过期的记录
        pipe.zremrangeByScore(key, 0, nowTs - period * 1000);
        // 获得当前用户时间窗口中的操作次数
        Response<Long> count = pipe.zcard(key);
        // 设置Key的过期时间，过了时间窗口自动删除
        pipe.expire(key, period + 1);
        // 执行上面的动作
        pipe.exec();
        pipe.close();

        return count.get() <= maxCount;
    }
}
