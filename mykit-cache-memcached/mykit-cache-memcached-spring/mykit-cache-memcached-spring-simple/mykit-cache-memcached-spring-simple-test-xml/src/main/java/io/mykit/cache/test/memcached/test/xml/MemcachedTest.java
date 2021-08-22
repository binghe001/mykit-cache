package io.mykit.cache.test.memcached.test.xml;

import io.mykit.cache.test.memcached.entity.Person;
import io.mykit.cache.test.memcached.service.MemcachedService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author binghe
 * @version 1.0.0
 * @description Memcached
 */
public class MemcachedTest {
    private final Logger logger = LoggerFactory.getLogger(MemcachedTest.class);

    private ClassPathXmlApplicationContext context;
    @Before
    public void init(){
        context = new ClassPathXmlApplicationContext("classpath*:spring/spring-context.xml");

    }

    @Test
    public void testMemcached() throws Exception{
        MemcachedService redisService = (MemcachedService) context.getBean("redisService");
        while (true) {
            String result = redisService.getRedidInfo("redis_test", "default_value1");
            logger.info(result);
            Thread.sleep(1000);
//			logger.info("===============================================");
//			result = redisService.getInfo("Hello World");
//			logger.info(result);
        }
    }
    @Test
    public void testMemcached1() throws Exception{
        MemcachedService redisService = (MemcachedService) context.getBean("redisService");
        String result = redisService.getRedidInfo("redis_test", "default_value1");
        logger.info(result);
        Thread.sleep(1000);
        logger.info("===============================================");
        result = redisService.getInfo("Hello World");
        logger.info(result);
    }
    @Test
    public void testPersons() throws Exception{
        MemcachedService redisService = (MemcachedService) context.getBean("redisService");
        while (true) {
            List<Person> list = redisService.getPersons("test");
            logger.info("获取到的列表长度：" + list.size());
            Thread.sleep(1000);
        }
    }
}
