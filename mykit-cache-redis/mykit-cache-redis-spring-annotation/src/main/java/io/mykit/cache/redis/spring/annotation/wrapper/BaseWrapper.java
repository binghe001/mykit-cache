package io.mykit.cache.redis.spring.annotation.wrapper;

import redis.clients.util.Hashing;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/21 21:43
 * @description 基础包装类
 */
public class BaseWrapper {

    /**
     * 计算缓存在ConcurrentMap中的key值
     * @param clazz: Class对象
     * @return 缓存在ConcurrentMap中的key值
     */
    public static String getContextKey(Class<?> clazz){
        return String.valueOf(Hashing.MURMUR_HASH.hash(clazz.getResource("/").getPath()));
    }
}
