package com.study.redis.apply.lesson8_practice;

import com.study.redis.apply.RedisApplyApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Hash
 * @since 2020/12/20
 */
public class BloomFilterTests extends RedisApplyApplicationTests {

    @Autowired
    RedisBloomFilter filter;

    @Test
    public void test() {
        for(int i = 0; i< 100000; i++) {
            filter.addElement("myBloomFilter", "user:"+i);
            boolean result = filter.exists("myBloomFilter", "user:"+i);
            if (!result) {
                System.out.println("user:"+i+"是否存在："+result);
            }
        }

    }
}
