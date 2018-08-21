package io.mykit.cache.redis.spring.annotation.utils;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/3 09:55
 * @description 获取默认的Redis Template
 */

public class RedisTemplateAnnotationUtils {
    private volatile static RedisTemplate redisTemplate;

    public static RedisTemplate getRedisTemplate(JedisConnectionFactory redisConnectionFactory) {
        if (redisTemplate == null) {
            synchronized (RedisTemplateAnnotationUtils.class) {
                if (redisTemplate == null) {
                    redisTemplate = new RedisTemplate();
                    redisTemplate.setConnectionFactory(redisConnectionFactory);

                    JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
                    redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
                    redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);

                    // 设置键（key）的序列化采用StringRedisSerializer。
                    redisTemplate.setKeySerializer(new StringRedisSerializer());
                    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
                    redisTemplate.afterPropertiesSet();
                }
            }

        }
        return redisTemplate;
    }
}
