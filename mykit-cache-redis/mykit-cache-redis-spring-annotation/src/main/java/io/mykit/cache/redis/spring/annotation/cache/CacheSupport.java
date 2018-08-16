package io.mykit.cache.redis.spring.annotation.cache;

import java.lang.reflect.Method;
import java.util.Map;
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
     * @param cacheName 缓存的名称
     * @param cacheKey 缓存的key
     */
    void refreshCacheByKey(String cacheName, String cacheKey);


    /**
     * 解析cacheable注解的value中的时间信息，以#分隔，第一个为缓存的key,第二个为缓存的有效时间，第三个为缓存的主动刷新时间
     * @param cacheName cacheable注解的value，格式为cachekey#expirationSecondTime#preloadSecondTime
     * @return 以CacheKey为Key,时间对象为value的Map
     */
    Map<String, CacheTime> getCacheTimes(String cacheName);


    /**
     * 解析cacheable注解的value中的实际CacheKey信息
     * @param cacheName cacheable注解的value信息
     * @return CacheKey信息
     */
    String getCacheKey(String cacheName);


}
