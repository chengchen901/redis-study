package com.study.redis.apply.lesson1.custom.aop;

import com.study.redis.apply.lesson1.custom.annotations.CoustomCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Hash
 * @since 2020/12/12
 */
@Aspect
@Component
public class CoustomCacheAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut("@annotation(com.study.redis.apply.lesson1.custom.annotations.CoustomCache)")
    public void cachePointcut() {
    }

    @Around("cachePointcut()")
    public Object doCache(ProceedingJoinPoint joinPoint) {
        Object value = null;
        try {
            // 0-1、 当前方法上注解的内容
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = joinPoint.getTarget().getClass().getMethod(signature.getName(), signature.getMethod().getParameterTypes());
            CoustomCache cacheAnnotation = method.getAnnotation(CoustomCache.class);
            String keyEl = cacheAnnotation.key();
            // 0-2、 前提条件：拿到作为key的依据  - 解析springEL表达式
            // 创建解析器
            ExpressionParser parser = new SpelExpressionParser();
            Expression expression = parser.parseExpression(keyEl);
            EvaluationContext context = new StandardEvaluationContext(); // 参数
            // 添加参数
            Object[] args = joinPoint.getArgs();
            DefaultParameterNameDiscoverer discover = new DefaultParameterNameDiscoverer();
            String[] parameterNames = discover.getParameterNames(method);
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i].toString());
            }
            // 解析
            String key = expression.getValue(context).toString();

            // 1、 判定缓存中是否存在
            value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                System.out.println("从缓存中读取到值：" + value);
                return value;
            }

            // 2、不存在则执行方法
            value = joinPoint.proceed();

            // 3、 同步存储value到缓存。
            redisTemplate.opsForValue().set(key, value);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return value;
    }
}
