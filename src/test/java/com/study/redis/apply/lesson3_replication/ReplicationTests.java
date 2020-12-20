package com.study.redis.apply.lesson3_replication;

import com.study.redis.apply.RedisApplyApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Hash
 * @since 2020/12/13
 */
public class ReplicationTests extends RedisApplyApplicationTests {

    @Autowired
    ReplicationDemoService replicationDemoService;

    @Test
    public void setTest() {
        replicationDemoService.setByCache("hash", "hahhhhh");

        String hash = replicationDemoService.getByCache("hash");
        System.out.println("主从复制获取到数据：" + hash);
    }
}
