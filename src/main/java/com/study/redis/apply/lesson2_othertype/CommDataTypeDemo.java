package com.study.redis.apply.lesson2_othertype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Hash
 * @since 2020/12/12
 */
@Service
public class CommDataTypeDemo {

    @Resource(name = "stringRedisTemplate")
    StringRedisTemplate redisTemplate;

    /**
     * 类似：在redis里面存储一个hashmap
     * 推荐的方式，无特殊需求是，一般的缓存都用这个。
     * 比如粉丝列表、关注列表、热门商品列表、热搜新闻列表
     */
    public void hash() {
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", "hash");
        user.put("age", 18);
        user.put("userId", 10001);
        System.out.println("map user 数据："+user);

        //redisTemplate.opsForHash().putAll("user_10001", user);

        redisTemplate.opsForHash().put("user_10001", "name", "hash");
        redisTemplate.opsForHash().put("user_10001", "age", "18");
        redisTemplate.opsForHash().put("user_10001", "userId", "10001");
        System.out.println("redis操作~~~~~");
        Map<?, ?> map = redisTemplate.opsForHash().entries("user_10001");
        System.out.println("redis结果："+map);
    }

    /**
     * 列表~ 集合数据存储~ java.util.List，java.util.Stack
     * 生产者消费者（简单MQ）
     */
    public void list() {
        // 从右侧，插入数据1 --- 2 --- 3
        // 从左侧插入，数据会怎样排列呢？
        redisTemplate.opsForList().rightPush("queue_1", "1");
        redisTemplate.opsForList().rightPush("queue_1", "1");
        // 在1这个值右边插入数据
        redisTemplate.opsForList().rightPush("queue_1", "1", "3");
        redisTemplate.opsForList().rightPush("queue_1", "4");

        List<String> list = redisTemplate.opsForList().range("queue_1", 0, -1);
        for (String item : list) {
            System.out.println("列表数据："+item);
        }

        // 消费者线程简例
        while (true) {
            String item = (String) redisTemplate.opsForList().leftPop("queue_1");
            if (item == null) break;
            System.out.println("从左侧消费数据："+item);
        }
    }

    /**
     * 用set实现（交集 并集）
     * 交集示例： 共同关注的好友
     * 并集示例：给同一个帖子，点赞的人+转发的人
     */
    public void set() {
        // 取出两个人共同关注的好友
        // 每个人维护一个set
        redisTemplate.opsForSet().add("user_hash", "userC", "userD", "userE");
        redisTemplate.opsForSet().add("user_peak", "userC", "userE", "userF");
        // 取出共同关注
        Set<String> sinter = redisTemplate.opsForSet().intersect("user_hash", "user_peak");
        System.out.println("共同关注了hash、peak的学员："+sinter);

        // 检索给某一个帖子点赞/转发的
        redisTemplate.opsForSet().add("trs_tp_1001", "userC", "userD", "userE");
        redisTemplate.opsForSet().add("star_tp_1001", "userE", "userF");

        // 取出共同人群
        Set<String> union = redisTemplate.opsForSet().union("star_tp_1001", "trs_tp_1001");
        System.out.println("点赞、转发了tp_1001贴子的人："+union);
    }

    /**
     * 游戏排行榜
     */
    public void zset() {
        // lol 得分排行
        String ranksKeyName = "lol_core";
        redisTemplate.opsForZSet().add(ranksKeyName, "redis", 100.0);
        redisTemplate.opsForZSet().add(ranksKeyName, "java", 82.0);
        redisTemplate.opsForZSet().add(ranksKeyName, "hash", 90.0);
        redisTemplate.opsForZSet().add(ranksKeyName, "map", 96.0);
        redisTemplate.opsForZSet().add(ranksKeyName, "ali", 89.0);

        Set<String> stringSet = redisTemplate.opsForZSet().reverseRange(ranksKeyName, 0, 2);
        System.out.println("返回前三名:");
        for (String s : stringSet) {
            System.out.println(s);
        }

        Long zcount = redisTemplate.opsForZSet().count(ranksKeyName, 85, 100);
        System.out.println("超过85分的数量：" + zcount);
    }
}
