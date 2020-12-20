package com.study.redis.apply.lesson8_practice;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 漏斗算法限流实现
 *
 * @author Hash
 * @since 2020/12/20
 */
@Service
public class JavaFunnelLimiter {
    // 这是一个漏斗
    static class Funnel {
        int capacity; // 漏斗容量，初始化大小
        float leakingRate; // 流水速率
        int leftQuota; // 剩余空间
        long lastLeakingTime; // 上一次漏水时间

        public Funnel(int capacity, float leakingRate) {
            this.capacity = capacity;
            this.leakingRate = leakingRate;
            this.leftQuota = capacity;
            this.lastLeakingTime = System.currentTimeMillis();
        }

        // 计算容量
        void countSpace() {
            long nowMill = System.currentTimeMillis();
            long deltaMill = nowMill - lastLeakingTime; // 距离上一次漏水过去了多久

            // 计算这段时间腾出了多少配额
            long deltaQuota = (long) (deltaMill * leakingRate);

            // 间隔时间太长，数字过大溢出了，恢复初始容量
            if (deltaQuota < 0) {
                this.leftQuota = capacity;
                this.lastLeakingTime = nowMill;
                return;
            }
            // 腾出空间太小，最小单位是 1，等下次，操作频率太快，
            if (deltaQuota < 1) {
                return;
            }

            // 容量足够
            this.leftQuota += deltaQuota; // 增加剩余空间
            this.lastLeakingTime = nowMill; // 记录漏水时间

            // 剩余空间不得高于容量
            if (this.leftQuota > this.capacity) {
                this.leftQuota = this.capacity;
            }
        }

        /**
         * 获取配额
         * @param quota
         * @return
         */
        boolean watering(int quota) {
            // 计算配额
            countSpace();

            // 减去此次配额
            if (this.leftQuota >= quota) {
                this.leftQuota -= quota;
                return true;
            }
            // 获得配额失败
            return false;
        }
    }

    // 管理所有的漏斗
    private Map<String, Funnel> funnels = new HashMap<>();

    // 是否允许访问
    public boolean isActionAllowed(String userId, String actionKey, int capacity, float leakingRate) {
        String key = String.format("%s:%s", userId, actionKey);
        // 获取已经创建过的漏斗
        Funnel funnel = funnels.get(key);

        // 创建新的漏斗
        if (funnel == null) {
            funnel = new Funnel(capacity, leakingRate);
            funnels.put(key, funnel);
        }

        // 需要 1 个流量
        return funnel.watering(1);
    }
}
