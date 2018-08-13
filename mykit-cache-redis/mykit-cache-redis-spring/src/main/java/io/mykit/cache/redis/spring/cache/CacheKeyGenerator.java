package io.mykit.cache.redis.spring.cache;

import io.mykit.cache.redis.spring.constants.CacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import redis.clients.util.Hashing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/3 16:15
 * @description 自定义的默认Key生成策略
 */
@Slf4j
public class CacheKeyGenerator implements KeyGenerator {
    // custom cache key
    public static final int NO_PARAM_KEY = 0;
    public static final int NULL_PARAM_KEY = 53;

    @Override
    public Object generate(Object target, Method method, Object... params) {

        StringBuilder key = new StringBuilder();
        key.append(target.getClass().getName()).append(".").append(method.getName()).append(":");
        if (params.length == 0) {
            return key.append(NO_PARAM_KEY).toString();
        }
        for (Object param : params) {
            if (param == null) {
                log.warn("input null param for Spring cache, use default key={}", NULL_PARAM_KEY);
                key.append(NULL_PARAM_KEY);
            } else if (ClassUtils.isPrimitiveArray(param.getClass())) {
                int length = Array.getLength(param);
                for (int i = 0; i < length; i++) {
                    key.append(Array.get(param, i));
                    key.append(',');
                }
            } else if (ClassUtils.isPrimitiveOrWrapper(param.getClass()) || param instanceof String) {
                key.append(param);
            } else {
                log.warn("Using an object as a cache key may lead to unexpected results. " +
                        "Either use @Cacheable(key=..) or implement CacheKey. Method is " + target.getClass() + "#" + method.getName());
                key.append(param.hashCode());
            }
            key.append('-');
        }
        String finalKey = key.toString();
        long cacheKeyHash = Hashing.MURMUR_HASH.hash(finalKey);
        log.debug("using cache key={} hashCode={}", finalKey, cacheKeyHash);
        return finalKey;
    }

    /**
     * 获取方法上的注解
     * @param method 方法句柄
     * @return 方法上的注解value属性，如果存在#, 则对value进行分割，获取分割的数组的第一个元素，如果不存在#则直接获取整个value属性
     */
    private String getCacheKey(Method method){
        String key = "";
        Cacheable annotation = method.getAnnotation(Cacheable.class);
        if(annotation != null){
            String[] values = annotation.value();
            if(values != null && values.length > 0){
                String value = "";
                for(String v : values){
                    if (!StringUtils.isEmpty(v)){
                        value = v;
                        break;
                    }
                }
                if (!StringUtils.isEmpty(value)){
                    if (value.contains(CacheConstants.SEPARATOR)){
                        key = value.split(CacheConstants.SEPARATOR)[0];
                    }else{
                        key = value;
                    }
                }
            }
        }
        return key;
    }
}
