package io.mykit.cache.redis.spring.cache;

import io.mykit.cache.redis.spring.lock.RedisLock;
import io.mykit.cache.redis.spring.utils.ThreadTaskUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/3 09:43
 * @description 自定义Redis Cache
 */
@Slf4j
public class CustomizedRedisCache extends RedisCache {

    private CacheSupport cacheSupport;

    private final RedisOperations redisOperations;

    private final byte[] prefix;

    /**
     * 缓存主动在失效前强制刷新缓存的时间
     * 单位：秒
     */
    private long preloadSecondTime = 0;

    /**
     * 缓存有效时间
     */
    private long expirationSecondTime;


    public CustomizedRedisCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration, long preloadSecondTime, CacheSupport cacheSupport) {
        super(name, prefix, redisOperations, expiration);
        this.redisOperations = redisOperations;
        // 指定有效时间
        this.expirationSecondTime = expiration;
        // 指定自动刷新时间
        this.preloadSecondTime = preloadSecondTime;
        this.prefix = prefix;
        this.cacheSupport = cacheSupport;
    }

    public CustomizedRedisCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration, long preloadSecondTime, boolean allowNullValues, CacheSupport cacheSupport) {
        super(name, prefix, redisOperations, expiration, allowNullValues);
        this.redisOperations = redisOperations;
        // 指定有效时间
        this.expirationSecondTime = expiration;
        // 指定自动刷新时间
        this.preloadSecondTime = preloadSecondTime;
        this.prefix = prefix;
        this.cacheSupport = cacheSupport;
    }

    /**
     * 重写get方法，获取到缓存后再次取缓存剩余的时间，如果时间小余我们配置的刷新时间就手动刷新缓存。
     * 为了不影响get的性能，启用后台线程去完成缓存的刷。
     * 并且只放一个线程去刷新数据。
     *
     * @param key  缓存的Key
     * @return ValueWrapper对象
     */
    @Override
    public ValueWrapper get(final Object key) {
        RedisCacheKey cacheKey = getRedisCacheKey(key);
        String cacheKeyStr = getCacheKey(key);
        //没有回调则进行回调方法
//        if (!callExpires){
//            this.callExpiresBack(cacheKeyStr);
//        }
        // 调用重写后的get方法
        ValueWrapper valueWrapper = this.get(cacheKey);

        if (null != valueWrapper) {
            log.debug("执行刷新缓存的方法...");
            // 刷新缓存数据
            refreshCache(key, cacheKeyStr);
        }
        return valueWrapper;
    }


    /**
     * 重写父类的get函数。
     * 父类的get方法，是先使用exists判断key是否存在，不存在返回null，存在再到redis缓存中去取值。这样会导致并发问题，
     * 假如有一个请求调用了exists函数判断key存在，但是在下一时刻这个缓存过期了，或者被删掉了。
     * 这时候再去缓存中获取值的时候返回的就是null了。
     * 可以先获取缓存的值，再去判断key是否存在。
     *
     * @param cacheKey 缓存的key
     * @return RedisCacheElement对象
     */
    @Override
    public RedisCacheElement get(final RedisCacheKey cacheKey) {

        Assert.notNull(cacheKey, "CacheKey must not be null!");

        // 根据key获取缓存值
        RedisCacheElement redisCacheElement = new RedisCacheElement(cacheKey, fromStoreValue(lookup(cacheKey)));
        // 判断key是否存在
        Boolean exists = (Boolean) redisOperations.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.exists(cacheKey.getKeyBytes());
            }
        });

        if (!exists.booleanValue()) {
            return null;
        }

        return redisCacheElement;
    }

    /**
     * 刷新缓存数据
     */
    private void refreshCache(Object key, final String cacheKeyStr) {
        Long ttl = this.redisOperations.getExpire(cacheKeyStr);
        log.debug("未获取到分布式锁：cacheKeyStr===>>>" +cacheKeyStr + ", ttl===>>>" + ttl + ",preloadSecondTime===>>>" + CustomizedRedisCache.this.preloadSecondTime);
        if (null != ttl && ttl <= CustomizedRedisCache.this.preloadSecondTime) {
            // 尽量少的去开启线程，因为线程池是有限的
            ThreadTaskUtils.run(new Runnable() {
                @Override
                public void run() {
                    // 加一个分布式锁，只放一个请求去刷新缓存
                    RedisLock redisLock = new RedisLock((RedisTemplate) redisOperations, cacheKeyStr + "_lock");
                    try {
                        if (redisLock.lock()) {
                            // 获取锁之后再判断一下过期时间，看是否需要加载数据
                            Long ttl = CustomizedRedisCache.this.redisOperations.getExpire(cacheKeyStr);
                            log.debug("获取到分布式锁：cacheKeyStr===>>>" +cacheKeyStr + ", ttl===>>>" + ttl + ",preloadSecondTime===>>>" + CustomizedRedisCache.this.preloadSecondTime);
                            if (null != ttl && ttl <= CustomizedRedisCache.this.preloadSecondTime) {
                                // 通过获取代理方法信息重新加载缓存数据
                                CustomizedRedisCache.this.cacheSupport.refreshCacheByKey(CustomizedRedisCache.super.getName(), cacheKeyStr);
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    } finally {
                        redisLock.unlock();
                    }
                }
            });
        }
    }

    public long getExpirationSecondTime() {
        return expirationSecondTime;
    }


    /**
     * 获取RedisCacheKey
     * @param key 缓存的Key
     * @return RedisCacheKey对象
     */
    public RedisCacheKey getRedisCacheKey(Object key) {

        return new RedisCacheKey(key).usePrefix(this.prefix)
                .withKeySerializer(redisOperations.getKeySerializer());
    }

    /**
     * 获取RedisCacheKey
     * @param key 缓存的Key
     * @return 缓存的value
     */
    public String getCacheKey(Object key) {
        return new String(getRedisCacheKey(key).getKeyBytes());
    }
}
