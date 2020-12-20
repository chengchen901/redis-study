package com.study.redis.apply.lesson5_cluster;

import com.study.redis.apply.RedisApplyApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @author Hash
 * @since 2020/12/19
 */
public class ClusterServiceTests extends RedisApplyApplicationTests {

    @Autowired
    ClusterService clusterService;

    // 测试cluster集群故障时的反应
    @Test
    public void failoverTest() {
        while (true) {
            try {
                long i = System.currentTimeMillis();
                clusterService.set("hash", i + "");
                // delay 10ms
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
