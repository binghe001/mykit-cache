package io.mykit.cache.test.redis.spring.service.impl;

import io.mykit.cache.test.redis.spring.service.RedisService;
import io.mykit.cache.test.redis.spring.utils.LoadFile;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service("redisService")  
public class RedisServiceImpl implements RedisService {
	//io.mykit.cache.test.redis.spring.service.impl.RedisServiceImpl.getRedidInfo:redis_test-default_value-
	@Override
	@Cacheable(value={"test11#5#3", "test22#6#4"} /*key="#key" + ".#defaultValue",*/)
	public String getRedidInfo(String key, String defaultValue) {
		return LoadFile.getValue(key);
	}

}
