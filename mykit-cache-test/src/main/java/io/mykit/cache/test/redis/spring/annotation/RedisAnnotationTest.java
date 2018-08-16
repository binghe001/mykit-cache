package io.mykit.cache.test.redis.spring.annotation;

import io.mykit.cache.redis.spring.annotation.config.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/16 11:32
 * @description 测试注解类型的缓存配置
 */
@Slf4j
public class RedisAnnotationTest {

    private AnnotationConfigApplicationContext context;

    @Before
    public void init(){
        context = new AnnotationConfigApplicationContext(RedisConfig.class);
    }

    @Test
    public void testConfig(){
        RedisConfig config = context.getBean(RedisConfig.class);
        log.info(config.getPassword() + "====>>>" + config.getMaxTotal());
    }
}
