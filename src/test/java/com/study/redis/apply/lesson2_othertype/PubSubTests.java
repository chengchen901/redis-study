package com.study.redis.apply.lesson2_othertype;

import com.study.redis.apply.RedisApplyApplicationTests;
import com.study.redis.apply.lesson2_othertype.pusub.MsgChannelListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Hash
 * @since 2020/12/12
 */
public class PubSubTests extends RedisApplyApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void testPusb() throws InterruptedException {
        System.out.println("开始测试发布订阅机制，6秒后发布一条消息");
        Thread.sleep(6000L);
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                // 发送通知
                Long received = connection.publish(MsgChannelListener.PUBSUB_CHANNEL_NAME.getBytes(), "{手机号码10086~短信内容~~}".getBytes());
                return received;
            }
        });
    }

    // 隐藏功能~~黑科技~~当key被删除，或者key过期之后，也会有通知~
    @Test
    public void testKeyDelEventChannel() throws InterruptedException {
        // 监听所有以键 hkkkk 为目标的所有事件
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.subscribe((message, pattern) -> {
                    System.out.println("通过Key删除事件通道，收到消息：" + message);
                }, "__keyspace@0__:hkkkk".getBytes());
                return null;
            }
        });

        redisTemplate.opsForValue().set("hkkkk", "hash");
        Thread.sleep(1000L);

        redisTemplate.delete("hkkkk");
    }
}
