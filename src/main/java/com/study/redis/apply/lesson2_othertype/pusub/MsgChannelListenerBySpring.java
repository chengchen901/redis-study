package com.study.redis.apply.lesson2_othertype.pusub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 接收消息通知
 *
 * @author Hash
 * @since 2020/12/12
 */
@Component
@Configuration
public class MsgChannelListenerBySpring {

    // 定义监听器
    @Bean
    public RedisMessageListenerContainer smsMessageListener(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        SmsSendListener smsSendListener = new SmsSendListener();
        container.addMessageListener(smsSendListener, Arrays.asList(new ChannelTopic(MsgChannelListener.PUBSUB_CHANNEL_NAME)));
        return container;
    }

    // 定义触发的方法
    class SmsSendListener implements MessageListener {
        @Override
        public void onMessage(Message message, byte[] pattern) {
            System.out.println("通道：" + new String(pattern));
            System.out.println("借助spring容器收到消息：" + message);
        }
    }
}
