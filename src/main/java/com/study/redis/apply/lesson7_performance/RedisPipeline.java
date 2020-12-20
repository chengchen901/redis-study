package com.study.redis.apply.lesson7_performance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hash
 * @since 2020/12/20
 */
@Component
public class RedisPipeline {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 使用pipeline的方式
     * @param batchSize
     */
    public void pipeline(int batchSize) {
        List<Object> results = stringRedisTemplate.executePipelined(
                new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        StringRedisConnection stringRedisConn = (StringRedisConnection)connection;
                        for(int i=0; i< batchSize; i++) {
                            // set key1 value2
                            // set key2 value2
                            stringRedisConn.set("pipeline"+i, "xxx"+i);
                        }
                        return null;
                    }
                });
        System.out.println("pipeline over. results: "+results);
    }

    /**
     * 使用简单的set命令
     * @param batchSize
     */
    public void setCommand(int batchSize) {
        for(int i=0; i< batchSize; i++) {
            stringRedisTemplate.opsForValue().set("pipeline"+i, "xxx"+i);
        }
        System.out.println("set command over");
    }
}
