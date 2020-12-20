package com.study.redis.apply.lesson3_replication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Hash
 * @since 2020/12/13
 */
@Service
public class ReplicationDemoService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void setByCache(String userId, String userInfo) {
        stringRedisTemplate.opsForValue().set(userId, userInfo);
    }

    public String getByCache(String userId) {
        return stringRedisTemplate.opsForValue().get(userId);
    }
}
