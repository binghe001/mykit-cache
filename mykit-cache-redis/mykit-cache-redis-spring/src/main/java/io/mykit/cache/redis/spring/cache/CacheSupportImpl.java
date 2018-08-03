package io.mykit.cache.redis.spring.cache;

import io.mykit.cache.redis.spring.cache.expression.CacheOperationExpressionEvaluator;
import io.mykit.cache.redis.spring.constants.CacheConstants;
import io.mykit.cache.redis.spring.utils.RedisTemplateUtils;
import io.mykit.cache.redis.spring.utils.ReflectionUtils;
import io.mykit.cache.redis.spring.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MethodInvoker;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/3 09:39
 * @description 手动刷新缓存实现类
 */
@Component
public class CacheSupportImpl implements CacheSupport {
    private static final Logger logger = LoggerFactory.getLogger(CacheSupportImpl.class);

    private final CacheOperationExpressionEvaluator evaluator = new CacheOperationExpressionEvaluator();

    @Autowired
    private KeyGenerator keyGenerator;

    @Autowired
    private RedisCacheManager cacheManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void registerInvocation(Object targetBean, Method targetMethod, Class[] invocationParamTypes,
                                   Object[] invocationArgs, Set<String> annotatedCacheNames, String cacheKey) {

        // 获取注解上真实的value值
        Collection<? extends Cache> caches = getCache(annotatedCacheNames);

        // 获取key的SpEL值
        Object key = generateKey(caches, cacheKey, targetMethod, invocationArgs, targetBean, CacheOperationExpressionEvaluator.NO_RESULT);

        // 新建一个代理对象（记录了缓存注解的方法类信息）
        final CachedMethodInvocation invocation = new CachedMethodInvocation(key, targetBean, targetMethod, invocationParamTypes, invocationArgs);
        for (Cache cache : caches) {
            if (cache instanceof CustomizedRedisCache) {
                CustomizedRedisCache redisCache = ((CustomizedRedisCache) cache);
                // 将方法信息放到redis缓存
                RedisTemplate redisTemplate = RedisTemplateUtils.getRedisTemplate(redisConnectionFactory);
                redisTemplate.opsForValue().set(getInvocationCacheKey(redisCache.getCacheKey(key)),
                        invocation, redisCache.getExpirationSecondTime(), TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public void refreshCacheByKey(String cacheName, String cacheKey) {
        RedisTemplate redisTemplate = RedisTemplateUtils.getRedisTemplate(redisConnectionFactory);
        //在redis拿到方法信息，然后刷新缓存
        Object result = redisTemplate.opsForValue().get(getInvocationCacheKey(cacheKey));

        if (result != null && result instanceof CachedMethodInvocation) {
            CachedMethodInvocation invocation = (CachedMethodInvocation) result;
            // 执行刷新方法
            refreshCache(invocation, cacheName);
        } else {
            logger.error("刷新redis缓存，反序列化方法信息异常");
        }

    }

    @Override
    public Map<String, CacheTime> getCacheTimes(String cacheName) {
        ConcurrentMap<String, CacheTime> map = new ConcurrentHashMap<>();
        //默认超时时间5分钟
        long expireTime = CacheConstants.DEFAULT_EXPIRATION_SECOND_TIME;
        //默认刷新缓存时间为280
        long preloadTime = CacheConstants.DEFAULT_PRELOAD_SECOND_TIME;
        //默认的缓存Key
        String cacheKey = CacheConstants.DEFAULT_EXPIRATION_KEY;
        //传递的参数不为空
        if(!StringUtils.isEmpty(cacheName)){
            if (cacheName.contains(CacheConstants.SEPARATOR)){
                String[] cacheArray = cacheName.split(CacheConstants.SEPARATOR);
                //设置@Cachable中的value中的key
                if (cacheArray.length > 0){
                    cacheKey = cacheArray[0].trim();
                }
                //设置@Cachable中的value中的expireTime
                if(cacheArray.length > 1){
                    try{
                        expireTime = Long.parseLong(cacheArray[1].trim());
                    }catch (NumberFormatException e){
                        expireTime = CacheConstants.DEFAULT_EXPIRATION_SECOND_TIME;
                    }
                }
                //设置@Cachable中的value中的preloadTime
                if(cacheArray.length > 2){
                    try{
                        preloadTime = Long.parseLong(cacheArray[2].trim());
                    }catch (NumberFormatException e){
                        preloadTime = CacheConstants.DEFAULT_PRELOAD_SECOND_TIME;
                    }
                }
            }else{
                cacheKey = cacheName.trim();
            }
        }
        map.put(cacheKey, new CacheTime(expireTime, preloadTime));
        return map;
    }

    @Override
    public String getCacheKey(String cacheName) {
        String cacheKey = CacheConstants.DEFAULT_EXPIRATION_KEY;
        //传递的参数不为空
        if (!StringUtils.isEmpty(cacheName)){
            if (cacheName.contains(CacheConstants.SEPARATOR)){
                String[] cacheArray = cacheName.split(CacheConstants.SEPARATOR);
                //设置@Cachable中的value中的key
                if (cacheArray.length > 0){
                    cacheKey = cacheArray[0].trim();
                }
            }else{
                cacheKey = cacheName.trim();
            }
        }
        return cacheKey;
    }

    private void refreshCache(CachedMethodInvocation invocation, String cacheName) {
        try {
            // 通过代理调用方法，并记录返回值
            Object computed = invoke(invocation);

            // 通过cacheManager获取操作缓存的cache对象
            Cache cache = cacheManager.getCache(cacheName);
            // 通过Cache对象更新缓存
            cache.put(invocation.getKey(), computed);

            RedisTemplate redisTemplate = RedisTemplateUtils.getRedisTemplate(redisConnectionFactory);
            CustomizedRedisCache redisCache = (CustomizedRedisCache) cache;
            long expireTime = redisCache.getExpirationSecondTime();
            // 刷新redis中缓存法信息key的有效时间
            redisTemplate.expire(getInvocationCacheKey(redisCache.getCacheKey(invocation.getKey())), expireTime, TimeUnit.SECONDS);

            logger.info("缓存：{}-{}，重新加载数据", cacheName, invocation.getKey().toString().getBytes());
        } catch (Exception e) {
            logger.info("刷新缓存失败：" + e.getMessage(), e);
        }

    }

    private Object invoke(CachedMethodInvocation invocation) throws Exception {

        // 获取执行方法所需要的参数
        Object[] args = null;
        if (!CollectionUtils.isEmpty(invocation.getArguments())) {
            args = invocation.getArguments().toArray();
        }
        // 通过先获取Spring的代理对象，在根据这个对象获取真实的实例对象
        Object target = ReflectionUtils.getTarget(SpringContextUtils.getBean(Class.forName(invocation.getTargetBean())));

        final MethodInvoker invoker = new MethodInvoker();
        invoker.setTargetObject(target);
        invoker.setArguments(args);
        invoker.setTargetMethod(invocation.getTargetMethod());
        invoker.prepare();

        return invoker.invoke();
    }

    /**
     * 解析SpEL表达式，获取注解上的key属性值
     * 直接扣的Spring解析表达式部分代码
     *
     * @return
     */
    protected Object generateKey(Collection<? extends Cache> caches, String key, Method method, Object[] args,
                                 Object target, Object result) {

        // 获取注解上的key属性值
        Class<?> targetClass = getTargetClass(target);
        if (org.springframework.util.StringUtils.hasText(key)) {
            EvaluationContext evaluationContext = evaluator.createEvaluationContext(caches, method, args, target,
                    targetClass, result, null);

            AnnotatedElementKey methodCacheKey = new AnnotatedElementKey(method, targetClass);
            return evaluator.key(key, methodCacheKey, evaluationContext);
        }
        return this.keyGenerator.generate(target, method, args);
    }

    /**
     * 获取类信息
     *
     * @param target
     * @return
     */
    private Class<?> getTargetClass(Object target) {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        if (targetClass == null && target != null) {
            targetClass = target.getClass();
        }
        return targetClass;
    }

    /**
     * 通过cache名称获取cache列表
     *
     * @param annotatedCacheNames
     * @return
     */
    public Collection<? extends Cache> getCache(Set<String> annotatedCacheNames) {

        Collection<String> cacheNames = generateValue(annotatedCacheNames);

        if (cacheNames == null) {
            return Collections.emptyList();
        } else {
            Collection<Cache> result = new ArrayList<Cache>();
            for (String cacheName : cacheNames) {
                Cache cache = this.cacheManager.getCache(cacheName);
                if (cache == null) {
                    throw new IllegalArgumentException("Cannot find cache named '" + cacheName + "' for ");
                }
                result.add(cache);
            }
            return result;
        }
    }

    private String getInvocationCacheKey(String cacheKey) {
        return cacheKey + CacheConstants.INVOCATION_CACHE_KEY_SUFFIX;
    }

    /**
     * 获取注解上的value属性值（cacheNames）
     *
     * @param annotatedCacheNames
     * @return
     */
    private Collection<String> generateValue(Set<String> annotatedCacheNames) {
        Collection<String> cacheNames = new HashSet<>();
        for (final String cacheName : annotatedCacheNames) {
            String[] cacheParams = cacheName.split(CacheConstants.SEPARATOR);
            // 截取名称获取真实的value值
            String realCacheName = cacheParams[0];
            cacheNames.add(realCacheName);
        }
        return cacheNames;
    }
}
