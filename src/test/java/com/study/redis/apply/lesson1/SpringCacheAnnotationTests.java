package com.study.redis.apply.lesson1;

import com.study.redis.apply.RedisApplyApplicationTests;
import com.study.redis.apply.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Hash
 * @since 2020/12/12
 */
public class SpringCacheAnnotationTests extends RedisApplyApplicationTests {

    @Autowired
    SpringCacheAnnotationService springCacheAnnotationService;

    // ---------------spring cache注解演示
    // get
    @Test
    public void springCacheTest() throws Exception {
        User user = springCacheAnnotationService.findUserById("hash");
        System.out.println(user);
    }

    @Test
    public void springCacheTest2() throws Exception {
        springCacheAnnotationService.updateUser(new User("hash", "hhhhhhh-2"));
        User user = springCacheAnnotationService.findUserById("hash");
        System.out.println(user);
    }

    @Test
    public void springCacheTest3() throws Exception {
        springCacheAnnotationService.deleteUserById("hash");
    }
}
