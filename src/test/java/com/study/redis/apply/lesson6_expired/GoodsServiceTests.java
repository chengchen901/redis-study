package com.study.redis.apply.lesson6_expired;

import com.study.redis.apply.RedisApplyApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Hash
 * @since 2020/12/20
 */
public class GoodsServiceTests extends RedisApplyApplicationTests {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsService2 goodsService2;

    @Autowired
    private GoodsService3 goodsService3;

    @Test
    public void testGoodsService() {
        int size = 10;
        final ExecutorService pool = Executors.newFixedThreadPool(size);
        CountDownLatch cdl = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            int goodsId = i;
            pool.execute(()->{
                try {
                    final Object o = goodsService.queryStock(1 + "");
                } finally {
                    cdl.countDown();
                }
            });
        }
        pool.shutdown();
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGoodsService2() {
        int size = 10;
        final ExecutorService pool = Executors.newFixedThreadPool(size);
        CountDownLatch cdl = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            pool.execute(()->{
                try {
                    final Object o = goodsService2.queryStock(2 + "");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    cdl.countDown();
                }
            });
        }
        pool.shutdown();
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGoodsService3() {
        int size = 10;
        final ExecutorService pool = Executors.newFixedThreadPool(size);
        CountDownLatch cdl = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            int goodsId = i;
            pool.execute(()->{
                try {
                    final Object o = goodsService3.queryStock(goodsId % 10 + "");
                } finally {
                    cdl.countDown();
                }
            });
        }
        pool.shutdown();
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
