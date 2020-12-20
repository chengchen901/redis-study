package com.study.redis.apply.lesson8_practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Redis定时通知
 *
 * @author Hash
 * @since 2020/12/20
 */
@Component
public class NoticeTimer {
    @Autowired
    StringRedisTemplate stringRedisTemplate; // redis客户端
    // 用于执行通知的线程池
    ExecutorService executor = Executors.newCachedThreadPool();

    // 需要做定时通知的资源
    public boolean addTimer(String resource, long time,Notify notify) {
        String resourceName = "notice_timer::" + resource;
        long timeout = time;
        // 初始化锁定资源

        Boolean result = stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                // 设置一个值
                Boolean status = connection.set(resourceName.getBytes(), "".getBytes(),
                        Expiration.seconds(timeout), RedisStringCommands.SetOption.SET_IF_ABSENT);
                return status;
            }
        });

        // 异步定时通知
        executor.execute(()-> {
            doNotify(resource, timeout, notify);
        });
        return result;
    }

    /**
     * 执行通知
     * @param resourceName 资源名称
     * @param timeout 定时时间
     * @param notify 通知执行体
     */
    public void doNotify(String resourceName, long timeout, Notify notify) {
        stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    CountDownLatch waiter = new CountDownLatch(1);
                    // 等待通知结果
                    connection.subscribe((message, pattern) -> {
                        // 收到通知，不管结果，立刻再次抢锁
                        waiter.countDown();


                        // 需要redis开启Key过期，通知事件
                    }, ("__keyspace@0__:"+resourceName+" del").getBytes());

                    // 等待一段时间，超过这个时间都没收到消息，肯定有问题
                    waiter.await(timeout+2, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 进行消息通知
                notify.notice();

                return true; //随便返回一个值都没问题
            }
        });


    }

    /**
     * 定时执行通知
     */
    interface Notify{
        // 通知
        void notice();
    }

}
