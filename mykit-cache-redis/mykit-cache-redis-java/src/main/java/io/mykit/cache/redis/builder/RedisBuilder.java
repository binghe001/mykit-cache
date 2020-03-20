package io.mykit.cache.redis.builder;

import io.mykit.cache.redis.config.LoadRedisProp;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis缓存
 */
public final class RedisBuilder {

    private volatile static JedisPool jedisPool;
    /**
     * 初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(LoadRedisProp.getIntegerValue(LoadRedisProp.MAX_IDLE));
            config.setMaxWaitMillis(LoadRedisProp.getIntegerValue(LoadRedisProp.MAX_WAIT));
            config.setTestOnBorrow(LoadRedisProp.getBooleanValue(LoadRedisProp.TEST_ON_BORROW));
            config.setMaxTotal(LoadRedisProp.getIntegerValue(LoadRedisProp.MAX_TOTAL));
            jedisPool = new JedisPool(config, LoadRedisProp.getStringValue(LoadRedisProp.HOST), LoadRedisProp.getIntegerValue(LoadRedisProp.PORT), LoadRedisProp.getIntegerValue(LoadRedisProp.TIMEOUT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取Jedis实例
     * @return
     */
    public synchronized static Jedis getInstance() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}