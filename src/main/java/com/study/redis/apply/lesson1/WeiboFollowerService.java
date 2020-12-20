package com.study.redis.apply.lesson1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Hash
 * @since 2020/12/12
 */
@Service
public class WeiboFollowerService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 收获微博粉丝一枚，路转粉
     * @param userId 用户Id
     * @return 粉丝数
     */
    public long addFollowers(String userId) {
        String key = "weibo:followers:"+userId;
        long num = stringRedisTemplate.opsForValue().increment(key);
        return num;
    }

    /**
     * 微博粉转路
     * @return 粉丝数
     */
    public long subFollowers(String userId) {
        String key = "weibo:followers:"+userId;
        long num = stringRedisTemplate.opsForValue().decrement(key);
        return num;
    }

    /**
     * 从缓存获取当前粉丝数
     * @param userId 用户Id
     * @return 粉丝数
     */
    public long followersFromCache(String userId) {
        String key = "weibo:followers:"+userId;
        String numStr = stringRedisTemplate.opsForValue().get(key);
        if(numStr == null) {
            // 省略数据库操作
            numStr = "0";
        }
        return Long.parseLong(numStr);
    }
}
