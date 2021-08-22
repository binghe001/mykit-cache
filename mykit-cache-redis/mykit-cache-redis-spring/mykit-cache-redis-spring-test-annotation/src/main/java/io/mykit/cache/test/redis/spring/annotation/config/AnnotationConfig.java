package io.mykit.cache.test.redis.spring.annotation.config;

import io.mykit.cache.redis.spring.annotation.config.CacheRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

/**
 * @author binghe
 * @version 1.0.0
 * @description 提供以Java注解的形式配置Spring和Redis集群整合的Spring容器管理
 */
@Configuration
@EnableCaching
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(value = {"io.mykit.cache"})
@PropertySource(value = {"classpath:properties/redis-default.properties", "classpath:properties/redis.properties"})
public class AnnotationConfig extends CacheRedisConfig {
}
