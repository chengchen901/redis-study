package com.study.redis.apply.lesson6_expired;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 从数据库中查询
 *
 * @author Hash
 * @since 2020/12/20
 */
@Component
public class DatabaseService {

    public String queryFromDatabase(String goodsId) {
        // 查询数据库...
        return "10";
    }
}
