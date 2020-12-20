package com.study.redis.apply.lesson8_practice;

import io.rebloom.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * RedisBloomFilter 插件客户端代码
 *
 * 项目参考地址：https://github.com/RedisBloom/JRedisBloom
 *
 * @author Hash
 * @since 2020/12/20
 */
@Service
public class RedisBloomFilter {

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;

    /** bloomfilter 客户端*/
    private Client client;

    @PostConstruct
    public void init() {
        // bloomfilter 客户端
        client = new Client(redisHost, redisPort);
    }

    /**
     * 创建一个自定义的过滤器
     * @param filterName
     */
    public void createFilter(String filterName) {
        // 创建一个容量10万，误判率0.01%的布隆过滤器
        client.createFilter(filterName, 100000, 0.0001);
    }

    /**
     * 添加元素
     * @param filterName
     * @param value
     * @return
     */
    public boolean addElement(String filterName, String value) {
        return client.add(filterName, value);
    }

    /**
     * 判断元素是否存在
     * @param filterName
     * @param value
     * @return
     */
    public boolean exists(String filterName, String value) {
        return client.exists(filterName, value);
    }

}
