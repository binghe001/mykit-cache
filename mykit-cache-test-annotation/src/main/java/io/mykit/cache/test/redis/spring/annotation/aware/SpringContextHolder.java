package io.mykit.cache.test.redis.spring.annotation.aware;

import io.mykit.cache.redis.spring.annotation.wrapper.SpringAnnotationContextWrapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/21 21:20
 * @description
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringAnnotationContextWrapper.setApplicationContext(SpringAnnotationContextWrapper.getContextKey(this.getClass()), applicationContext);
    }
}
