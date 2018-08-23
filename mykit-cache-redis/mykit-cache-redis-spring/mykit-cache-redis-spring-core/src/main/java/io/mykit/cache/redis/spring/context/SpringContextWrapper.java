package io.mykit.cache.redis.spring.context;

import org.springframework.context.ApplicationContext;
import redis.clients.util.Hashing;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/14 10:01
 * @description 封装SpringContext
 */
public class SpringContextWrapper {


    /**
     * 存储各个调用方的ApplicationContext的map, key为计算出的调用方的hashCode值， value为调用方对应的ApplicationContext
     */
    private volatile static ConcurrentMap<String, ApplicationContext> instance;

    static {
        instance = new ConcurrentHashMap<String, ApplicationContext>();
    }

    /**
     * 将各模块对应的ApplicationContext存到map中
     * @param contextKey  计算出的模块的唯一hash值
     * @param applicationContext 模块对应的ApplicationContext
     */
    public static void setApplicationContext(String contextKey, ApplicationContext applicationContext) {
        //缓存中不存在相关的applicationContext，存将applicationContext放入到缓存
        if (!instance.containsKey(contextKey)){
            instance.put(contextKey, applicationContext);
        }
    }

    /**
     * 取得存储在map缓存中的ApplicationContext.
     * @param contextKey 计算出的模块的唯一hash值
     * @return ApplicationContext对象
     */
    public static ApplicationContext getApplicationContext(String contextKey) {
        checkApplicationContext();
        return instance.get(contextKey);
    }

    /**
     * 从缓存map中的ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * @param contextKey 计算出的模块的唯一hash值
     * @param name Spring中Bean的名称
     * @return 泛型对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String contextKey, String name) {
        checkApplicationContext();
        return (T) instance.get(contextKey).getBean(name);
    }

    /**
     * 从缓存map中的ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * @param contextKey 计算出的模块的唯一hash值
     * @param clazz 指定的clazz对象
     * @return 泛型对象
     */
    public static <T> T getBean(String contextKey, Class<T> clazz) {
        checkApplicationContext();
        return (T) instance.get(contextKey).getBean(clazz);
    }

    /**
     * 清除缓存Map中的所有ApplicationContext实例
     */
    public static void cleanApplicationContext() {
        instance.clear();
    }

    /**
     * 检测缓存map中是否存在ApplicationContext实例
     */
    private static void checkApplicationContext() {
        if (instance == null || instance.size() == 0) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
        }
    }


    /**
     * 计算缓存在ConcurrentMap中的key值
     * @param clazz: Class对象
     * @return 缓存在ConcurrentMap中的key值
     */
    public static String getContextKey(Class<?> clazz){
        return String.valueOf(Hashing.MURMUR_HASH.hash(clazz.getResource("/").getPath()));
    }

}
