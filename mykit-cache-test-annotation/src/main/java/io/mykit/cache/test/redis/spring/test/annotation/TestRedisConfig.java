package io.mykit.cache.test.redis.spring.test.annotation;

import io.mykit.cache.test.redis.spring.annotation.config.AnnotationConfig;
import io.mykit.cache.test.redis.spring.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/21 18:24
 * @description 测试配置
 */
@Slf4j
public class TestRedisConfig {

    private AnnotationConfigApplicationContext context;

    @Before
    public void init(){
        context = new AnnotationConfigApplicationContext(AnnotationConfig.class);
    }

    @Test
    public void testRedis() throws Exception{
        RedisService redisService = (RedisService) context.getBean("redisService");
        while (true) {
            String result = redisService.getRedidInfo("redis_test", "default_value1");
            log.info(result);
            Thread.sleep(1000);
        }
    }
}
