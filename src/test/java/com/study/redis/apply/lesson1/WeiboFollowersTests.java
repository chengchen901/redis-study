package com.study.redis.apply.lesson1;

import com.study.redis.apply.RedisApplyApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Hash
 * @since 2020/12/12
 */
public class WeiboFollowersTests extends RedisApplyApplicationTests {

    @Autowired
    WeiboFollowerService followerService;

    @Test
    public void followers() {
        long followerNum = followerService.addFollowers("hash");
        System.out.println("路转粉，收获妹子一枚："+followerNum);

        followerNum = followerService.subFollowers("hash");
        System.out.println("粉转路，妹子离我而去："+followerNum);

        followerService.followersFromCache("hash");
        System.out.println("获取所有粉丝数："+followerNum);

    }
}
