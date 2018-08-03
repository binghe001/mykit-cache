package io.mykit.cache.redis.spring.cache;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/3 09:33
 * @description 注册和刷新缓存接口
 */
public interface CacheSupport {
    /**
     * 注册缓存方法信息
     *
     * @param invokedBean          代理Bean
     * @param invokedMethod        代理方法名称
     * @param invocationParamTypes 代理方法参数类型
     * @param invocationArgs       代理方法参数
     * @param cacheNames           缓存名称（@Cacheable注解的value）
     * @param cacheKey             缓存key（@Cacheable注解的key）
     */
    void registerInvocation(Object invokedBean, Method invokedMethod, Class[] invocationParamTypes, Object[] invocationArgs, Set<String> cacheNames, String cacheKey);

    /**
     * 按容器以及指定键更新缓存
     *
     * @param cacheName
     * @param cacheKey
     */
    void refreshCacheByKey(String cacheName, String cacheKey);
}
