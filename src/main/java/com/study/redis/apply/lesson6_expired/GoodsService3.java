package com.study.redis.apply.lesson6_expired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 1、 不同商品，要分开
/**
 * 1.锁细化
 * 2.缓存降级：Redis备份缓存解决方案
 * 3.服务降级
 *
 * @author Hash
 * @since 2020/12/20
 */
@Component
public class GoodsService3 {

    private final Logger logger = LoggerFactory.getLogger(GoodsService3.class);

    @Resource(name = "mainStringRedisTemplate")
    StringRedisTemplate mainRedisTemplate;

    @Resource(name = "backStringRedisTemplate")
    StringRedisTemplate backRedisTemplate;

    @Autowired
    DatabaseService databaseService;

    // k/v存储 ： 205  -> 锁的状态
    Map<String, String> mapLock = new ConcurrentHashMap<>();


    /**
     * 查询商品库存数
     *
     * @param goodsId 商品ID
     * @return 商品库存数
     */
    // @Cacheable 不管用什么样的方式，核心步骤 1，2，3
    public Object queryStock(final String goodsId) {
        // 1. 先从redis缓存中获取信息
        String cacheKey = "goodsStock-"+goodsId;
        String value = mainRedisTemplate.opsForValue().get(cacheKey);
        if (value != null) {
            logger.warn(Thread.currentThread().getName() + "缓存中取得数据==============>" + value);
            return value;
        }

        // 2000
        boolean lock = false;
        try {
            // setnx
            lock = mapLock.putIfAbsent(goodsId, "xxoo") == null;
            if (lock) { // 2000线程，一个抢到锁
                value = mainRedisTemplate.opsForValue().get(cacheKey);
                if (value != null) {
                    logger.warn(Thread.currentThread().getName() + "缓存中取得数据==============>" + value);
                    return value;
                }

                // 缓存重建
                // 2. 缓存中没有,则取数据库
                value = databaseService.queryFromDatabase(goodsId);
                System.out.println(Thread.currentThread().getName() + "从数据库中取得数据==============>" + value);

                // 3. 塞到主缓存,120秒过期时间
                final String v = value;
                mainRedisTemplate.execute((RedisCallback<Boolean>) connection -> {
                    return connection.setEx(cacheKey.getBytes(), 120, v.getBytes());
                });

                // 4. 双写，写入备份缓存
                backRedisTemplate.opsForValue().set(cacheKey, value);

            } else { // 没拿到锁的怎么办？
                // 服务降级：11.11，12.12 高峰期 支付宝/淘宝/....提示稍后再付款/ 排队中 / 客官慢一点
                // 不断地降低预期目标 ： 买早餐，肉包子_>菜包子-> 烧麦 -- 馒头 ---
                // 缓存降级: 根据业务场景，去判断，选择合适的降级
                // 1、 重试 争抢锁 （控制重试的次数）
                // 2、 直接返回一个空
                // 3、 备用缓存
                value = backRedisTemplate.opsForValue().get(cacheKey);
                if (value != null) {
                    System.out.println(Thread.currentThread().getName() + "缓存降级：备份缓存==============>" + value);
                } else {
                    value = "0";
                    System.out.println(Thread.currentThread().getName() + "缓存降级：返回固定值==============>" + value);
                }
            }
        } finally {
            if (lock) {
                mapLock.remove(goodsId);
            }
        }
        return value;
    }
}
