package com.study.redis.apply.lesson5_cluster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Hash
 * @since 2020/12/19
 */
@Service
public class ClusterService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void set(String userId, String userInfo) {
        stringRedisTemplate.opsForValue().set(userId, userInfo);
    }
}
