package io.mykit.cache.test.redis.spring.annotation.config;

import io.mykit.cache.redis.spring.annotation.config.CacheRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/21 21:38
 * @description
 */
@Configuration
@EnableCaching
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(value = {"io.mykit.cache.redis.spring.annotation", "io.mykit.cache.test.redis.spring"})
@PropertySource(value = {"classpath:properties/redis-default.properties", "classpath:properties/redis.properties"})
public class AnnotationConfig extends CacheRedisConfig {
}
