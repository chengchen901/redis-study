package com.study.redis.apply.lesson2_othertype.pusub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 接收消息通知，直接用客户端的方式
 *
 * @author Hash
 * @since 2020/12/12
 */
@Component
public class MsgChannelListener {

    @Autowired
    RedisTemplate redisTemplate;

    /** 用于测试的通道名称 */
    public final static String PUBSUB_CHANNEL_NAME = "msg_channel";

    @PostConstruct
    public void setup() {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.subscribe((message, pattern) -> {
                    System.out.println("使用redisTemplate收到消息：" + message);
                }, MsgChannelListener.PUBSUB_CHANNEL_NAME.getBytes());
                return null;
            }
        });
    }
}
