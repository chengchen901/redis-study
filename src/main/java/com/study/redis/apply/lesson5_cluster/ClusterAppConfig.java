package com.study.redis.apply.lesson5_cluster;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.util.Arrays;

/**
 * @author Hash
 * @since 2020/12/19
 */
//@Configuration
public class ClusterAppConfig {

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        System.out.println("加载cluster环境下的redis client配置");
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(Arrays.asList(
                "192.168.254.161:6381",
                "192.168.254.161:6382",
                "192.168.254.161:6383",
                "192.168.254.161:6384",
                "192.168.254.161:6385",
                "192.168.254.161:6386"
        ));
        // 自适应集群变化
        return new JedisConnectionFactory(redisClusterConfiguration);
    }
}
