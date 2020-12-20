package com.study.redis.apply.lesson2_othertype;

import com.study.redis.apply.RedisApplyApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Hash
 * @since 2020/12/12
 */
public class CommTypeTests extends RedisApplyApplicationTests {

    @Autowired
    CommDataTypeDemo service;

    // 类似：在redis里面存储一个hashmap
    @Test
    public void hashTest() {
        service.hash();
    }

    // 列表~ 集合数据存储~ java.util.List，java.util.Stack
    @Test
    public void list() {
        service.list();
    }

    // 用set实现（交集 并集）
    @Test
    public void setTest() {
        service.set();
    }

    // 游戏排行榜
    @Test
    public void zsetTest() {
        service.zset();
    }
}
