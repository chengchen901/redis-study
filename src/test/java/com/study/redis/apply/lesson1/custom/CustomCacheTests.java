package com.study.redis.apply.lesson1.custom;

import com.study.redis.apply.RedisApplyApplicationTests;
import com.study.redis.apply.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Hash
 * @since 2020/12/12
 */
public class CustomCacheTests extends RedisApplyApplicationTests {

    @Autowired
    CustomAnnoDemoService customDemoService;

    // get
    @Test
    public void springCacheTest() throws Exception {
        User user = customDemoService.findUserById("wahaha");
        System.out.println(user);
    }
}
