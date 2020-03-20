/**
 * Copyright 2020-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mykit.cache.redis.test;

import io.mykit.cache.redis.builder.RedisBuilder;
import io.mykit.cache.redis.builder.RedisClusterBuilder;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

/**
 * @author binghe
 * @version 1.0.0
 * @description 测试缓存
 */
public class TestCache {

    @Test
    public void testSaveRedisCache(){
        JedisCluster jedisCluster = RedisClusterBuilder.getInstance();
        jedisCluster.set("name", "测试Redis缓存");
        System.out.println("保存成功");
    }
    @Test
    public void testGetRedisCache(){
        JedisCluster jedisCluster = RedisClusterBuilder.getInstance();
        String value = jedisCluster.get("name");
        System.out.println("从Redis中获取的缓存数据为：" + value);
    }

    @Test
    public void testSaveSingleRedisCache(){
        Jedis jedis = RedisBuilder.getInstance();
        jedis.set("name", "binghe");
        String value = jedis.get("name");
        System.out.println(value);
    }
}
