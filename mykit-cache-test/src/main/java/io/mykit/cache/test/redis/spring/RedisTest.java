package io.mykit.cache.test.redis.spring;

import io.mykit.cache.test.redis.spring.service.RedisService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;


/**
 * 测试Redis缓存
 * @author liuyazhuang
 *
 */
@Component
public class RedisTest {
	
	private final Logger logger = LoggerFactory.getLogger(RedisTest.class);
	
	private ClassPathXmlApplicationContext context;
	@Before
	public void init(){
		context = new ClassPathXmlApplicationContext("spring/spring-context.xml");
		
	}
	
	@Test
	public void testRedis() throws Exception{
		RedisService redisService = (RedisService) context.getBean("redisService");
		while (true) {
			String result = redisService.getRedidInfo("redis_test", "default_value1");
			logger.info(result);
			Thread.sleep(1000);
			logger.info("===============================================");
			result = redisService.getInfo("Hello World");
			logger.info(result);
		}
	}
}
