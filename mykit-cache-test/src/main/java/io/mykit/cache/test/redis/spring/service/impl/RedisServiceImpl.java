package io.mykit.cache.test.redis.spring.service.impl;

import io.mykit.cache.test.redis.spring.service.RedisService;
import io.mykit.cache.test.redis.spring.utils.LoadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import redis.clients.util.Hashing;

@Slf4j
@Service("redisService")  
public class RedisServiceImpl implements RedisService {
	//io.mykit.cache.test.redis.spring.service.impl.RedisServiceImpl.getRedidInfo:redis_test-default_value-
	@Override
	@Cacheable(value={"test111#15#2"} /*key="#key" + ".#defaultValue",*/)
	public String getRedidInfo(String key, String defaultValue) {
		log.debug(RedisServiceImpl.class.getName() + "类加载的路径：" + this.getClass().getResource("/").getPath()+ ", hashcode:" + Hashing.MURMUR_HASH.hash(this.getClass().getResource("/").getPath()));
		return LoadFile.getValue(key);
	}

	@Override
	public void printInfo() {
		log.info("success");
	}

}
