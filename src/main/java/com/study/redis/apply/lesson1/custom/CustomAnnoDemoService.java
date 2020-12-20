package com.study.redis.apply.lesson1.custom;

import com.study.redis.apply.lesson1.custom.annotations.CoustomCache;
import com.study.redis.apply.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Hash
 * @since 2020/12/12
 */
@Service
public class CustomAnnoDemoService {

    @Autowired
    private RedisTemplate redisTemplate;

    @CoustomCache(key = "#userId")
    public User findUserById(String userId) throws Exception {
        User user = null;
        user = new User(userId, "张三");
        System.out.println("从数据库中读取到值：" + user);
        return user;
    }
}
