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


    private volatile static ConcurrentMap<String, ApplicationContext> instance;

    static {
        instance = new ConcurrentHashMap<String, ApplicationContext>();
    }

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     */
    public static void setApplicationContext(String contextKey, ApplicationContext applicationContext) {
        //缓存中不存在相关的applicationContext，存将applicationContext放入到缓存
        if (!instance.containsKey(contextKey)){
            instance.put(contextKey, applicationContext);
        }
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     * @return ApplicationContext对象
     */
    public static ApplicationContext getApplicationContext(String contextKey) {
        checkApplicationContext();
        return instance.get(contextKey);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * @param name Spring中Bean的名称
     * @return 泛型对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String contextKey, String name) {
        checkApplicationContext();
        return (T) instance.get(contextKey).getBean(name);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * @param clazz 指定的clazz对象
     * @return 泛型对象
     */
    public static <T> T getBean(String contextKey, Class<T> clazz) {
        checkApplicationContext();
        return (T) instance.get(contextKey).getBean(clazz);
    }

    /**
     * 清除applicationContext静态变量.
     */
    public static void cleanApplicationContext() {
        instance.clear();
    }

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
