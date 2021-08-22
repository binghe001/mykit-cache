package io.mykit.cache.redis.spring.constants;

/**
 * @author binghe
 * @version 1.0.0
 * @description 缓存常量类
 */
public class CacheConstants {

    /**
     * Cacheable注解的value中的分隔符
     */
    public static final String SEPARATOR = "#";

    /**
     * 拆分缓存Key的分隔符
     */
    public static final String SEPARATOE_SPLIT = ":";

    /**
     * 缓存后缀
     */
    public static final String INVOCATION_CACHE_KEY_SUFFIX = ":invocation_cache_key_suffix";

    /**
     * 父类dynamic字段
     */
    public static final String SUPER_FIELD_DYNAMIC = "dynamic";

    /**
     * 父类cacheNullValues字段
     */
    public static final String SUPER_FIELD_CACHENULLVALUES = "cacheNullValues";

    /**
     * 默认缓存的Key
     */
    public static final String DEFAULT_EXPIRATION_KEY = "defaultExpirationKey";

    /**
     * 默认的缓存过期时间 300秒
     */
    public static final long DEFAULT_EXPIRATION_SECOND_TIME = 300;

    /**
     * 默认的自动刷新缓存时间 280秒
     */
    public static final long DEFAULT_PRELOAD_SECOND_TIME = 280;


}
