package io.mykit.cache.redis.spring.annotation.cache;


import io.mykit.cache.redis.spring.annotation.constants.CacheAnnotationConstants;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/14 11:48
 * @description 缓存的支持工具类
 */
public class CacheAnnotationSupportUtils {

    /**
     * 获取InvocationCacheKey
     * @param cacheKey 缓存的Key
     * @return InvocationCacheKey
     */
    public static String getInvocationCacheKey(String cacheKey) {
        return cacheKey + CacheAnnotationConstants.INVOCATION_CACHE_KEY_SUFFIX;
    }
}
