package io.mykit.cache.test.redis.spring.service;

/**
 * 测试缓存
 * @author liuyazhuang
 *
 */
public interface RedisService {
	
	String getRedidInfo(String key, String defaultValue);

	void printInfo();
}
