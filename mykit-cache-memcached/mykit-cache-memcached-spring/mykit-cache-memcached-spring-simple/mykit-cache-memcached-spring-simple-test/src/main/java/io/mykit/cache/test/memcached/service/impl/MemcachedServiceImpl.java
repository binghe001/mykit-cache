package io.mykit.cache.test.memcached.service.impl;

import com.google.code.ssm.api.ParameterValueKeyProvider;
import com.google.code.ssm.api.ReadThroughSingleCache;
import io.mykit.cache.test.memcached.entity.Person;
import io.mykit.cache.test.memcached.service.MemcachedService;
import io.mykit.cache.test.memcached.utils.LoadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("redisService")  
public class MemcachedServiceImpl implements MemcachedService {
	@Override
	@ReadThroughSingleCache(namespace="intimeCache", expiration = 5)
	public String getRedidInfo(@ParameterValueKeyProvider String key, String defaultValue) {
		return LoadFile.getValue(key);
	}

	@Override
	@ReadThroughSingleCache(namespace="intimeCache", expiration = 5)
	public String getInfo(@ParameterValueKeyProvider String info){
		log.debug("进入了方法....");
		return "info===>>>" + info;
	}

	@Override
	@ReadThroughSingleCache(namespace="intimeCache", expiration = 5)
	public List<Person> getPersons(@ParameterValueKeyProvider String params) {
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
