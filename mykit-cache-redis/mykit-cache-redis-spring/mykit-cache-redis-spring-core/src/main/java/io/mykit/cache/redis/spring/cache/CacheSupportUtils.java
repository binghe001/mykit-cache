package io.mykit.cache.redis.spring.cache;

import io.mykit.cache.redis.spring.constants.CacheConstants;

/**
 * @author binghe
 * @version 1.0.0
 * @description 缓存的支持工具类
 */
public class CacheSupportUtils {

    /**
     * 获取InvocationCacheKey
     * @param cacheKey 缓存的Key
     * @return InvocationCacheKey
     */
    public static String getInvocationCacheKey(String cacheKey) {
        return cacheKey + CacheConstants.INVOCATION_CACHE_KEY_SUFFIX;
    }
}
