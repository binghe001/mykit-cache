package io.mykit.cache.redis.spring.annotation.config;

import com.alibaba.fastjson.parser.ParserConfig;
import io.mykit.cache.redis.spring.annotation.aspect.SpringCachingAnnotationsAspect;
import io.mykit.cache.redis.spring.annotation.cache.CacheAnnotationKeyGenerator;
import io.mykit.cache.redis.spring.annotation.cache.CacheAnnotationTime;
import io.mykit.cache.redis.spring.annotation.cache.CustomizedAnnotationRedisCacheManager;
import io.mykit.cache.redis.spring.annotation.serializer.FastJsonRedisAnnotationSerializer;
import io.mykit.cache.redis.spring.annotation.serializer.StringRedisAnnotationSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/21 19:06
 * @description Redis配置类
 */
@Slf4j
public class CacheRedisConfig extends BaseRedisConfig{

    /**
     * 配置 JedisPoolConfig
     * @return JedisPoolConfig对象
     */
    @Bean
    public JedisPoolConfig jedisAnnotationPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        jedisPoolConfig.setTestWhileIdle(testWhileIdle);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        return jedisPoolConfig;
    }

    /**
     * 配置 RedisClusterConfiguration
     * @return RedisClusterConfiguration对象
     */
    @Bean
    public RedisClusterConfiguration redisAnnotationClusterConfiguration(){
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.setMaxRedirects(3);
        redisClusterConfiguration.setClusterNodes(getRedisNodes());
        return redisClusterConfiguration;
    }

    /**
     * 配置 JedisConnectionFactory
     * @return 返回JedisConnectionFactory对象
     */
    @Bean
    public JedisConnectionFactory jedisAnnotationConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisAnnotationClusterConfiguration(), jedisAnnotationPoolConfig());
        jedisConnectionFactory.setPassword(password);
        jedisConnectionFactory.setTimeout(timeout);
        return jedisConnectionFactory;
    }


    /**
     * 配置RedisTemplate
     * @return RedisTemplate对象
     */
    @Bean
    public RedisTemplate<String, Object> redisAnnotationTemplate(){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(jedisAnnotationConnectionFactory());
        StringRedisAnnotationSerializer keySerializer = new StringRedisAnnotationSerializer();
        FastJsonRedisAnnotationSerializer<Object> valueSerializer = new FastJsonRedisAnnotationSerializer<Object>();
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        return redisTemplate;
    }

    /**
     * 配置CacheAnnotationTime
     * @return CacheAnnotationTime对象
     */
    @Bean
    public CacheAnnotationTime cacheAnnotationTime(){
        CacheAnnotationTime cacheAnnotationTime = new CacheAnnotationTime(expirationSecondTime, preloadSecondTime);
        return cacheAnnotationTime;
    }

    /**
     * 配置 CacheAnnotationKeyGenerator
     * @return CacheAnnotationKeyGenerator对象
     */
    @Bean
    public CacheAnnotationKeyGenerator cacheAnnotationKeyGenerator(){
        CacheAnnotationKeyGenerator cacheAnnotationKeyGenerator = new CacheAnnotationKeyGenerator();
        return cacheAnnotationKeyGenerator;
    }

    /**
     * 配置SpringCachingAnnotationsAspect
     * @return SpringCachingAnnotationsAspect对象
     */
    @Bean
    public SpringCachingAnnotationsAspect springCachingAnnotationsAspect(){
        SpringCachingAnnotationsAspect springCachingAnnotationsAspect = new SpringCachingAnnotationsAspect();
        return springCachingAnnotationsAspect;
    }


    /**
     * 配置CustomizedAnnotationRedisCacheManager
     * @return CustomizedAnnotationRedisCacheManager对象
     */
    @Bean
    public CustomizedAnnotationRedisCacheManager customizedAnnotationRedisCacheManager(){
        CustomizedAnnotationRedisCacheManager customizedAnnotationRedisCacheManager = new CustomizedAnnotationRedisCacheManager(redisAnnotationTemplate());
        customizedAnnotationRedisCacheManager.setDefaultExpiration(redisDefaultExpiration);
        customizedAnnotationRedisCacheManager.setUsePrefix(usePrefix);
        Map<String, CacheAnnotationTime> map = new HashMap<String, CacheAnnotationTime>();
        map.put(defaultExpirationKey, cacheAnnotationTime());
        customizedAnnotationRedisCacheManager.setCacheTimes(map);
        return customizedAnnotationRedisCacheManager;
    }


    /**
     * 封装各Redis节点信息
     * @return Redis节点Set集合
     */
    private Set<RedisNode> getRedisNodes(){
        Set<RedisNode> set = new HashSet<RedisNode>();
        RedisNode redisNode1 = new RedisNode(nodeOne, nodeOnePort);
        set.add(redisNode1);

        RedisNode redisNode2 = new RedisNode(nodeTwo, nodeTwoPort);
        set.add(redisNode2);

        RedisNode redisNode3 = new RedisNode(nodeThree, nodeThreePort);
        set.add(redisNode3);

        RedisNode redisNode4 = new RedisNode(nodeFour, nodeFourPort);
        set.add(redisNode4);

        RedisNode redisNode5 = new RedisNode(nodeFive, nodeFivePort);
        set.add(redisNode5);

        RedisNode redisNode6 = new RedisNode(nodeSix, nodeSixPort);
        set.add(redisNode6);

        RedisNode redisNode7 = new RedisNode(nodeSeven, nodeSevenPort);
        set.add(redisNode7);
        return set;
    }

    @Override
    public CacheManager cacheManager() {
        return customizedAnnotationRedisCacheManager();
    }

    @Override
    public KeyGenerator keyGenerator() {
        return cacheAnnotationKeyGenerator();
    }

    @Override
    public CacheResolver cacheResolver() {
        return super.cacheResolver();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {

            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                RedisErrorException(exception, key);
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                RedisErrorException(exception, key);
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                RedisErrorException(exception, key);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                RedisErrorException(exception, null);
            }
        };
        return cacheErrorHandler;
    }

    protected void RedisErrorException(Exception exception,Object key){
        log.error("redis异常：key=[{}], exception={}", key, exception.getMessage());
    }
}
