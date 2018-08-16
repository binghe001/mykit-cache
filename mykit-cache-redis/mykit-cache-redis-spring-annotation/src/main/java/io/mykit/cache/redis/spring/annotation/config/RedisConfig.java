package io.mykit.cache.redis.spring.annotation.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/16 11:08
 * @description 全局配置类
 */
@Data
@Configuration
@ComponentScan(value = {"io.mykit.cache.redis.spring.annotation"})
@PropertySource(value = {"classpath:properties/redis-default.properties", "classpath:properties/redis.properties"})
public class RedisConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigure(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Redis集群密码
     */
    @Value("${redis.cluster.password}")
    private String password;

    /**
     * 集群最大连接数
     */
    @Value("${redis.cluster.max.total}")
    private Integer maxTotal;


    /**
     * 集群最大的idle数量
     */
    @Value("${redis.cluster.max.idle}")
    private Integer maxIdle;


    /**
     * 集群最小idle数量
     */
    @Value("${redis.cluster.min.idle}")
    private Integer minIdle;

    /**
     * 超时时间
     */
    @Value("${redis.cluster.timeout}")
    private Integer timeOut;

    /**
     * maxAttempts
     */
    @Value("${redis.cluster.maxAttempts}")
    private Integer maxAttempts;

    /**
     * 默认超时时间
     */
    @Value("${redis.cluster.redisDefaultExpiration}")
    private Integer defaultExpiration;

    /**
     * 是否使用前缀
     */
    @Value("${redis.cluster.usePrefix}")
    private Boolean usePrefix;

    /**
     * 耗尽时是否阻塞
     */
    @Value("${redis.cluster.blockWhenExhausted}")
    private Boolean blockWhenExhausted;

    /**
     * 最大等待时间
     */
    @Value("${redis.cluster.maxWaitMillis}")
    private Integer maxWaitMillis;
}
