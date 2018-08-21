package io.mykit.cache.redis.spring.annotation.config;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/21 18:48
 * @description Redis集群配置基础类
 */
@Data
public class BaseRedisConfig extends CachingConfigurerSupport {

    @Value("${redis.cluster.max.total}")
    protected Integer maxTotal;

    @Value("${redis.cluster.max.idle}")
    protected Integer maxIdle;

    @Value("${redis.cluster.min.idle}")
    protected Integer minIdle;

    @Value("${redis.cluster.timeout}")
    protected Integer timeout;

    @Value("${redis.cluster.maxAttempts}")
    protected Integer maxAttempts;

    @Value("${redis.cluster.redisDefaultExpiration}")
    protected Integer redisDefaultExpiration;

    @Value("${redis.cluster.usePrefix}")
    protected Boolean usePrefix;

    @Value("${redis.cluster.blockWhenExhausted}")
    protected Boolean blockWhenExhausted;

    @Value("${redis.cluster.maxWaitMillis}")
    protected Integer maxWaitMillis;

    @Value("${redis.cluster.testOnBorrow}")
    protected Boolean testOnBorrow;

    @Value("${redis.cluster.testOnReturn}")
    protected Boolean testOnReturn;

    @Value("${redis.cluster.testWhileIdle}")
    protected Boolean testWhileIdle;

    @Value("${redis.cluster.minEvictableIdleTimeMillis}")
    protected Integer minEvictableIdleTimeMillis;

    @Value("${redis.cluster.timeBetweenEvictionRunsMillis}")
    protected Integer timeBetweenEvictionRunsMillis;

    @Value("${redis.cluster.numTestsPerEvictionRun}")
    protected Integer numTestsPerEvictionRun;

    @Value("${redis.cluster.defaultExpirationKey}")
    protected String defaultExpirationKey;

    @Value("${redis.cluster.expirationSecondTime}")
    protected Integer expirationSecondTime;

    @Value("${redis.cluster.preloadSecondTime}")
    protected Integer preloadSecondTime;

    @Value("${redis.cluster.index.zero}")
    protected Integer zero;

    @Value("${redis.cluster.index.one}")
    protected Integer one;

    @Value("${redis.cluster.index.two}")
    protected Integer two;

    @Value("${redis.cluster.index.three}")
    protected Integer three;

    @Value("${redis.cluster.node.one}")
    protected String nodeOne;

    @Value("${redis.cluster.node.one.port}")
    protected Integer nodeOnePort;

    @Value("${redis.cluster.node.two}")
    protected String nodeTwo;

    @Value("${redis.cluster.node.two.port}")
    protected Integer nodeTwoPort;

    @Value("${redis.cluster.node.three}")
    protected String nodeThree;

    @Value("${redis.cluster.node.three.port}")
    protected Integer nodeThreePort;

    @Value("${redis.cluster.node.four}")
    protected String nodeFour;

    @Value("${redis.cluster.node.four.port}")
    protected Integer nodeFourPort;

    @Value("${redis.cluster.node.five}")
    protected String nodeFive;

    @Value("${redis.cluster.node.five.port}")
    protected Integer nodeFivePort;

    @Value("${redis.cluster.node.six}")
    protected String nodeSix;

    @Value("${redis.cluster.node.six.port}")
    protected Integer nodeSixPort;

    @Value("${redis.cluster.node.seven}")
    protected String nodeSeven;

    @Value("${redis.cluster.node.seven.port}")
    protected Integer nodeSevenPort;

    @Override
    public String toString(){
        return JSONObject.toJSONString(this);
    }

}
