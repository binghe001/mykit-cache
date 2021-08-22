package io.mykit.cache.test.redis.spring.utils;

import io.mykit.cache.redis.spring.context.SpringContextWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import redis.clients.util.Hashing;

/**
 * @author binghe
 * @version 1.0.0
 * @description 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候中取出ApplicaitonContext.
 */
@Slf4j
@Component
public class SpringContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.applicationContext = applicationContext; // NOSONAR
        log.debug(SpringContext.class.getName() + " 类加载的路径：" + this.getClass().getResource("/").getPath() + ", hashcode:" + Hashing.MURMUR_HASH.hash(this.getClass().getResource("/").getPath()));
        log.debug(SpringContext.class.getName() + " applicationContext===>>>" + applicationContext);
        SpringContextWrapper.setApplicationContext(SpringContextWrapper.getContextKey(this.getClass()), applicationContext);
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     * @return ApplicationContext对象
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * @param name Spring中Bean的名称
     * @return 泛型对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * @param clazz 指定的clazz对象
     * @return 泛型对象
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return (T) applicationContext.getBean(clazz);
    }

    /**
     * 清除applicationContext静态变量.
     */
    public static void cleanApplicationContext() {
        applicationContext = null;
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
        }
    }
}
