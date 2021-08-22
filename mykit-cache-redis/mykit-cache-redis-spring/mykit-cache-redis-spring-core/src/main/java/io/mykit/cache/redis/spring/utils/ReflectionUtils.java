package io.mykit.cache.redis.spring.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author binghe
 * @version 1.0.0
 * @description 方法类
 */
@Slf4j
public class ReflectionUtils {

    /**
     * 获取targetBean所在类，实现的包含targetMethod方法的接口类
     * @param targetBean 类的全类名
     * @param targetMethod 方法
     * @return targetBean实现的包含targetMethod的接口，当前类无接口，则直接返回
     */
    public static Class<?> getInterfaceClass(String targetBean, String targetMethod, List<String> parameterTypes) throws Exception{
        Class<?> clazz = Class.forName(targetBean);
        Class<?>[] parametersType = getParameterTypes(parameterTypes);
        Class<?>[] interfaces = clazz.getInterfaces();
        //没有实现接口，直接返回
        if(interfaces == null || interfaces.length == 0){
            return clazz;
        }
        for(Class<?> c : interfaces){
            Method method = c.getMethod(targetMethod, parametersType);
            if (method != null){
                clazz = c;
                break;
            }

            method = c.getDeclaredMethod(targetMethod, parametersType);
            if (method != null){
                clazz = c;
                break;
            }
        }

        return clazz;
    }

    private static Class<?>[] getParameterTypes(List<String> parameterTypes) throws Exception{
        if(parameterTypes == null || parameterTypes.size() == 0){
            return new Class<?>[0];
        }
        Class<?>[] classes = new Class<?>[parameterTypes.size()];
        for(int i = 0; i < parameterTypes.size(); i++){
            classes[i] = Class.forName(parameterTypes.get(i));
        }
        return classes;
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredMethod
     *
     * @param object         : 子类对象
     * @param methodName     : 父类中的方法名
     * @param parameterTypes : 父类中的方法参数类型
     * @return 父类中的方法对象
     */

    public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
        Method method = null;

        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了

            }
        }

        return null;
    }

    /**
     * 直接调用对象方法, 而忽略修饰符(private, protected, default)
     *
     * @param object         : 子类对象
     * @param methodName     : 父类中的方法名
     * @param parameterTypes : 父类中的方法参数类型
     * @param parameters     : 父类中的方法参数
     * @return 父类中方法的执行结果
     */

    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes,
                                      Object[] parameters) {
        //根据 对象、方法名和对应的方法参数 通过反射 调用上面的方法获取 Method 对象
        Method method = getDeclaredMethod(object, methodName, parameterTypes);

        //抑制Java对方法进行检查,主要是针对私有方法而言
        method.setAccessible(true);
        try {
            if (null != method) {

                //调用object 的 method 所代表的方法，其方法的参数是 parameters
                return method.invoke(object, parameters);
            }
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     *
     * @param object    : 子类对象
     * @param fieldName : 父类中的属性名
     * @return 父类中的属性对象
     */

    public static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;

        Class<?> clazz = object.getClass();

        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了

            }
        }

        return null;
    }

    /**
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
     *
     * @param object    : 子类对象
     * @param fieldName : 父类中的属性名
     * @param value     : 将要设置的值
     */

    public static void setFieldValue(Object object, String fieldName, Object value) {

        //根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);

        //抑制Java对其的检查
        field.setAccessible(true);

        try {
            //将 object 中 field 所代表的值 设置为 value
            field.set(object, value);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }

    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param object    : 子类对象
     * @param fieldName : 父类中的属性名
     * @return : 父类中的属性值
     */

    public static Object getFieldValue(Object object, String fieldName) {

        //根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);

        //抑制Java对其的检查
        field.setAccessible(true);

        try {
            //获取 object 中 field 所代表的属性值
            return field.get(object);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 获取 目标对象
     *
     * @param proxy 代理对象
     * @return 目标对象
     * @throws Exception 抛出Exception对象
     */
    public static Object getTarget(Object proxy) throws Exception {
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;//不是代理对象
        }

        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        } else { //cglib
            return getCglibProxyTargetObject(proxy);
        }
    }


    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        return target;
    }


    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
        return target;
    }
}
