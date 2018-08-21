package io.mykit.cache.test.redis.spring.service.impl;

import io.mykit.cache.test.redis.spring.entity.Person;
import io.mykit.cache.test.redis.spring.service.RedisService;
import io.mykit.cache.test.redis.spring.utils.LoadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import redis.clients.util.Hashing;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("redisService")  
public class RedisServiceImpl implements RedisService {
	//io.mykit.cache.test.redis.spring.service.impl.RedisServiceImpl.getRedidInfo:redis_test-default_value-
	@Override
	@Cacheable(value={"test111#10#2"} /*key="#key" + ".#defaultValue",*/)
	public String getRedidInfo(String key, String defaultValue) {
		log.debug(RedisServiceImpl.class.getName() + "类加载的路径：" + this.getClass().getResource("/").getPath()+ ", hashcode:" + Hashing.MURMUR_HASH.hash(this.getClass().getResource("/").getPath()));
		return LoadFile.getValue(key);
	}

	@Override
	@Cacheable(value={"test111#10#2"} /*key="#key" + ".#defaultValue",*/)
	public String getInfo(String info){
		log.debug("进入了方法....");
		return "info===>>>" + info;
	}

	@Override
	@Cacheable(value={"RedisServiceImpl.getPersons#10#2"} /*key="#key" + ".#defaultValue",*/)
	public List<Person> getPersons() {
		log.info("进入了获取用户列表的方法...");
		List<Person> persons = new ArrayList<>();
		persons.add(new Person("lyz1", 21));
		persons.add(new Person("lyz2", 22));
		persons.add(new Person("lyz3", 23));
		persons.add(new Person("lyz4", 24));
		persons.add(new Person("lyz5", 25));
		return persons;
	}

}
