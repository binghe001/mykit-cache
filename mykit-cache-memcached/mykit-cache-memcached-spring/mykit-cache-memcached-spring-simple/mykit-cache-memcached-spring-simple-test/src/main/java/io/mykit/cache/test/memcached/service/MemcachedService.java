package io.mykit.cache.test.memcached.service;

import io.mykit.cache.test.memcached.entity.Person;
import io.mykit.cache.test.memcached.entity.Person;

import java.util.List;

/**
 * 测试缓存
 * @author binghe
 *
 */
public interface MemcachedService {
	
	String getRedidInfo(String key, String defaultValue);

	String getInfo(String info);

	List<Person> getPersons(String params);
}
