package io.mykit.cache.test.redis.spring.annotation;

import io.mykit.cache.redis.spring.annotation.config.CacheRedisConfig;
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
        context = new AnnotationConfigApplicationContext(CacheRedisConfig.class);
    }

    @Test
    public void testConfig(){
        CacheRedisConfig config = context.getBean(CacheRedisConfig.class);
        log.info(String.valueOf(config));
    }
}
