package com.study.redis.apply.lesson1.custom.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义cache注解
 *
 * @author Hash
 * @since 2020/12/12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CoustomCache {

    /**
     * key的规则，可以使用springEL表达式，可以使用方法执行的一些参数
     */
    String key();
}
