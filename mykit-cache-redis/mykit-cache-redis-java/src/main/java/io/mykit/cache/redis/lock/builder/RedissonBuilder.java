package io.mykit.cache.redis.lock.builder;

import io.mykit.cache.redis.config.LoadRedisProp;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @author binghe
 * @version 1.0.0
 * @description Redission分布式锁配置
 */
public class RedissonBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedissonBuilder.class);

	private static volatile RedissonClient instance;

	static{
		try {
			instance = redissonClient();
		} catch (IOException e) {
			LOGGER.error("初始化Redisson客户端失败，抛出异常：{}", e);
		}
	}
	
    private static RedissonClient redissonClient() throws IOException {
		Config config = new Config();
		//指定编码，默认编码为org.redisson.codec.JsonJacksonCodec
		//之前使用的spring-data-redis，用的客户端jedis，编码为org.springframework.data.redis.serializer.StringRedisSerializer
		//改用redisson后为了之间数据能兼容，这里修改编码为org.redisson.client.codec.StringCodec
		config.setCodec(new org.redisson.client.codec.StringCodec());
		//指定使用集群部署方式
		config.useClusterServers()
			// 集群状态扫描间隔时间，单位是毫秒
		    .setScanInterval(2000) 
		    //cluster方式至少6个节点(3主3从，3主做sharding，3从用来保证主宕机后可以高可用)
		    .addNodeAddress(LoadRedisProp.getResissonClusterUrl(LoadRedisProp.getStringValue(LoadRedisProp.CLUSTER_NODE_ONE), LoadRedisProp.getIntegerValue(LoadRedisProp.CLUSTER_NODE_ONE_PORT)))
		    .addNodeAddress(LoadRedisProp.getResissonClusterUrl(LoadRedisProp.getStringValue(LoadRedisProp.CLUSTER_NODE_TWO), LoadRedisProp.getIntegerValue(LoadRedisProp.CLUSTER_NODE_TWO_PORT)))
		    .addNodeAddress(LoadRedisProp.getResissonClusterUrl(LoadRedisProp.getStringValue(LoadRedisProp.CLUSTER_NODE_THREE), LoadRedisProp.getIntegerValue(LoadRedisProp.CLUSTER_NODE_THREE_PORT)))
		    .addNodeAddress(LoadRedisProp.getResissonClusterUrl(LoadRedisProp.getStringValue(LoadRedisProp.CLUSTER_NODE_FOUR), LoadRedisProp.getIntegerValue(LoadRedisProp.CLUSTER_NODE_FOUR_PORT)))
		    .addNodeAddress(LoadRedisProp.getResissonClusterUrl(LoadRedisProp.getStringValue(LoadRedisProp.CLUSTER_NODE_FIVE), LoadRedisProp.getIntegerValue(LoadRedisProp.CLUSTER_NODE_FIVE_PORT)))
		    .addNodeAddress(LoadRedisProp.getResissonClusterUrl(LoadRedisProp.getStringValue(LoadRedisProp.CLUSTER_NODE_SIX), LoadRedisProp.getIntegerValue(LoadRedisProp.CLUSTER_NODE_SIX_PORT)));
		return Redisson.create(config);
    }

    public static RedissonClient getInstance(){
		return instance;
	}
	
}
