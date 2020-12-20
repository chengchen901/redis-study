package com.study.redis.apply.lesson8_practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 令牌桶Redis实现
 * 分布式下，防止超卖的场景，
 *
 * @author Hash
 * @since 2020/12/20
 */
@Component
public class TokenBukect {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 创建令牌桶
     * @param tokenName
     * @param tokenSize
     */
    public void createToken(String tokenName, int tokenSize){
        String name = "token_list:"+tokenName;
        for(int i =0; i < tokenSize; i++){
            stringRedisTemplate.opsForList().leftPush(name, "token_"+i);
        }
        System.out.println(tokenSize+"个令牌，生成完毕！");
    }

    /**
     * 使用令牌
     * @return
     */
    public String useToken(String tokenName) {
        String name = "token_list:"+tokenName;
        String token = stringRedisTemplate.opsForList().leftPop(name);
        return token;
    }

}
