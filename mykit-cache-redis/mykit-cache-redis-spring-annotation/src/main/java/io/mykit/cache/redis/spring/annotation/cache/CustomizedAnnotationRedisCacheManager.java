package io.mykit.cache.redis.spring.annotation.cache;

import com.alibaba.fastjson.JSONObject;
import io.mykit.cache.redis.spring.annotation.constants.CacheAnnotationConstants;
import io.mykit.cache.redis.spring.annotation.wrapper.SpringAnnotationContextWrapper;
import io.mykit.cache.redis.spring.annotation.utils.ReflectionAnnotationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/3 09:46
 * @description
 * 自定义的redis缓存管理器
 * 支持方法上配置过期时间
 * 支持热加载缓存：缓存即将过期时主动刷新缓存
 */
@Slf4j
public class CustomizedAnnotationRedisCacheManager extends RedisCacheManager {

    private RedisCacheManager redisCacheManager = null;

    private CacheAnnotationSupport getCacheSupport() {
        return SpringAnnotationContextWrapper.getBean(SpringAnnotationContextWrapper.getContextKey(CacheAnnotationSupport.class), CacheAnnotationSupport.class);
    }

    // 0 - never expire
    private long defaultExpiration = 0;

    private volatile Map<String, CacheAnnotationTime> cacheTimes = null;

    public CustomizedAnnotationRedisCacheManager(RedisOperations redisOperations) {
        super(redisOperations);
    }

    public CustomizedAnnotationRedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames) {
        super(redisOperations, cacheNames);
    }

    public RedisCacheManager getInstance() {
        if (redisCacheManager == null) {
            redisCacheManager = SpringAnnotationContextWrapper.getBean(SpringAnnotationContextWrapper.getContextKey(RedisCacheManager.class), RedisCacheManager.class);
        }
        return redisCacheManager;
    }

    /**
     * 获取过期时间
     * @param name 缓存名称
     * @return 过期时间
     */
    public long getExpirationSecondTime(String name) {
        if (StringUtils.isEmpty(name)) {
            return 0;
        }

        CacheAnnotationTime cacheTime = null;
        if (!CollectionUtils.isEmpty(cacheTimes)) {
            cacheTime = cacheTimes.get(name);
        }
        Long expiration = cacheTime != null ? cacheTime.getExpirationSecondTime() : defaultExpiration;
        return expiration < 0 ? 0 : expiration;
    }

    /**
     * 获取自动刷新时间
     * @param name 缓存名称
     * @return 自动刷新时间
     */
    private long getPreloadSecondTime(String name) {
        // 自动刷新时间，默认是0
        CacheAnnotationTime cacheTime = null;
        if (!CollectionUtils.isEmpty(cacheTimes)) {
            cacheTime = cacheTimes.get(name);
        }
        Long preloadSecondTime = cacheTime != null ? cacheTime.getPreloadSecondTime() : 0;
        return preloadSecondTime < 0 ? 0 : preloadSecondTime;
    }

    /**
     * 创建缓存
     * @param cacheName 缓存名称
     * @return CustomizedRedisCache对象
     */
    @Override
    public CustomizedAnnotationRedisCache getMissingCache(String cacheName) {
        CacheAnnotationSupport cacheSupport = this.getCacheSupport();
        String cacheKey = cacheSupport.getCacheKey(cacheName);
        Map<String, CacheAnnotationTime> map = cacheSupport.getCacheTimes(cacheName);
        log.debug("getMissingCache方法调用cacheSupport.getCacheTimes()方法获取的结果Map为===>>>" + JSONObject.toJSONString(map));
        //处理cacheTimes和Map
        this.putMapToCacheTimes(map);
        // 有效时间，初始化获取默认的有效时间
        Long expirationSecondTime = getExpirationSecondTime(cacheKey);
        // 自动刷新时间，默认是0
        Long preloadSecondTime = getPreloadSecondTime(cacheKey);

        log.debug("目前cacheTimes中存储的数据为：{}", JSONObject.toJSONString(cacheTimes));
        log.debug("缓存 cacheName：{}，过期时间:{}, 自动刷新时间:{}", cacheKey, expirationSecondTime, preloadSecondTime);
        // 是否在运行时创建Cache
        Boolean dynamic = (Boolean) ReflectionAnnotationUtils.getFieldValue(getInstance(), CacheAnnotationConstants.SUPER_FIELD_DYNAMIC);
        // 是否允许存放NULL
        Boolean cacheNullValues = (Boolean) ReflectionAnnotationUtils.getFieldValue(getInstance(), CacheAnnotationConstants.SUPER_FIELD_CACHENULLVALUES);
        return dynamic ? new CustomizedAnnotationRedisCache(cacheKey, (this.isUsePrefix() ? this.getCachePrefix().prefix(cacheKey) : null),
                this.getRedisOperations(), expirationSecondTime, preloadSecondTime, cacheNullValues, cacheSupport) : null;
    }

    /**
     * 将cacheTimes中不存在的Key，但是map中存在的Key的CacheTime对象加入到cacheTimes中
     * @param map cacheSupport.getCacheTimes()方法获取到的map
     */
    private void putMapToCacheTimes(Map<String, CacheAnnotationTime> map){
        if (!CollectionUtils.isEmpty(map)){
            if (cacheTimes != null){
                //遍历map
                for(Map.Entry<String, CacheAnnotationTime> entry : map.entrySet()){
                    //cacheTimes不存在map中的key
                    if(!cacheTimes.containsKey(entry.getKey())){
                        cacheTimes.put(entry.getKey(), entry.getValue());
                    }
                }
            }else{
                setCacheTimes(map);
            }
        }
    }

    /**
     * 根据缓存名称设置缓存的有效时间和刷新时间，单位秒
     *
     * @param cacheTimes 缓存的名称和时间集合
     */
    public void setCacheTimes(Map<String, CacheAnnotationTime> cacheTimes) {
        log.debug("setCacheTimes===>>>" + JSONObject.toJSONString(cacheTimes));
        this.cacheTimes = (cacheTimes != null ? new ConcurrentHashMap<String, CacheAnnotationTime>(cacheTimes) : null);
    }

    /**
     * 设置默认的过期时间， 单位：秒
     * @param defaultExpireTime 默认过期时间
     */
    @Override
    public void setDefaultExpiration(long defaultExpireTime) {
        super.setDefaultExpiration(defaultExpireTime);
        this.defaultExpiration = defaultExpireTime;
    }

    /**
     * 设置缓存的Key和对应的过期时间
     * @param expires 缓存的key和对应的过期时间组成的Map
     */
    @Override
    public void setExpires(Map<String, Long> expires) {
        super.setExpires(expires);
    }
}
