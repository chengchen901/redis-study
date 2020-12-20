package com.study.redis.apply.lesson3_replication;

import com.study.redis.apply.RedisApplyApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Hash
 * @since 2020/12/13
 */
public class ReplicationRWTests extends RedisApplyApplicationTests {

    @Autowired
    ReplicationDemoService replicationDemoService;

    @Test
    public void setTest() {
        // set主库写入
        replicationDemoService.setByCache("hash", "xxxx");
        // get从库读取
        String result = replicationDemoService.getByCache("hash");
        System.out.println("从缓存中读取到数据：" + result);
    }
}
