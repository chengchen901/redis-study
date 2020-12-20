package com.study.redis.apply.lesson6_expired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 使用Redis缓存的执行过程
 *
 * @author Hash
 * @since 2020/12/20
 */
@Component
public class GoodsService {

    private final Logger logger = LoggerFactory.getLogger(GoodsService.class);

    @Resource(name = "mainStringRedisTemplate")
    StringRedisTemplate mainRedisTemplate;

    @Autowired
    DatabaseService databaseService;

    /**
     * 查询商品库存数
     *
     * @param goodsId 商品ID
     * @return 商品库存数
     */
    // @Cacheable 不管用什么样的方式，核心步骤 1，2，3
    public Object queryStock(final String goodsId) {
        String cacheKey = "goodsStock-"+goodsId;
        // 1. 先从redis缓存中获取信息
        String value = mainRedisTemplate.opsForValue().get(cacheKey);
        if (value != null) {
            logger.warn(Thread.currentThread().getName() + "缓存中取得数据==============>" + value);
            return value;
        }
        // 2. 缓存中没有,则取数据库
        value = databaseService.queryFromDatabase(goodsId);
        System.out.println(Thread.currentThread().getName() + "从数据库中取得数据==============>" + value);

        // 3. 塞到缓存,120秒过期时间
        final String v = value;
        mainRedisTemplate.execute((RedisCallback<Boolean>) connection -> {
            return connection.setEx(cacheKey.getBytes(), 120, v.getBytes());
        });

        return value;
    }
}
