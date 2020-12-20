package com.study.redis.apply.lesson8_practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 高速队列
 * Stream
 *
 * @author Hash
 * @since 2020/12/20
 */
public class QuickMQ {
    private int capacity;
    @Autowired
    StringRedisTemplate stringRedisTemplate; // redis客户端

    public void put() {

    }

    public void remove() {

    }
}
