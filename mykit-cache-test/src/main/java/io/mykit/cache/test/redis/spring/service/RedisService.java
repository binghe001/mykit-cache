package io.mykit.cache.test.redis.spring.service;

import io.mykit.cache.test.redis.spring.entity.Person;

import java.util.List;

/**
 * 测试缓存
 * @author liuyazhuang
 *
 */
public interface RedisService {
	
	String getRedidInfo(String key, String defaultValue);

	String getInfo(String info);

	List<Person> getPersons();
}
