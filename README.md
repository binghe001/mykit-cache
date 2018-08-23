# 作者简介: 
Adam Lu(刘亚壮)，高级软件架构师，Java编程专家，开源分布式消息引擎Mysum发起者、首席架构师及开发者，Android开源消息组件Android-MQ独立作者，国内知名开源分布式数据库中间件Mycat核心架构师、开发者，精通Java, C, C++, Python, Hadoop大数据生态体系，熟悉MySQL、Redis内核，Android底层架构。多年来致力于分布式系统架构、微服务、分布式数据库、大数据技术的研究，曾主导过众多分布式系统、微服务及大数据项目的架构设计、研发和实施落地。在高并发、高可用、高可扩展性、高可维护性和大数据等领域拥有丰富的经验。对Hadoop、Spark、Storm等大数据框架源码进行过深度分析并具有丰富的实战经验。

# 作者联系方式
QQ：2711098650

# 项目简述
mykit架构中独立出来的mykit-cache组件，封装了mykit架构下对于缓存cache的各种操作，使用者只需要引入相关的Jar包，即可实现对缓存的轻松操作。

# 项目结构描述
封装了对于缓存的操作，支持Memcached、Redis、Ehcache等分布式缓存数据库，同时支持Spring的注解，通过Spring的注解可实现设置缓存的失效时间和主动刷新缓存

## mykit-cache-memcached
mykit-cache架构下与Memcached缓存相关的组件

### mykit-cache-memcached-spring
mykit-cache-memcached 下主要与 Spring 整合 Memcached 操作相关的组件，支持通过注解设置缓存有效时间

## mykit-cache-redis
mykit-cache架构下与Redis缓存相关的组件

### mykit-cache-redis-spring
mykit-cache-redis 下主要与 Spring 整合 Redis操作相关的组件，支持通过注解设置缓存有效时间和主动刷新缓存，  
主要提供Spring整合Redis的通用工具方法等，核心实现由此模块提供

### mykit-cache-redis-spring-annotation
mykit-cache-redis 下主要与 Spring 整合 Redis操作相关的组件，支持通过注解设置缓存有效时间和主动刷新缓存，  
主要以Java注解的形式实现Spring容器的管理操作，兼容Redis集群宕机或其他原因无法连接Redis集群时的情况，  
如果Redis集群宕机或其他原因无法连接Redis集群时，打印相关的日志，并继续向下执行原有方法。

### mykit-cache-redis-spring-xml
mykit-cache-redis 下主要与 Spring 整合 Redis操作相关的组件，支持通过注解设置缓存有效时间和主动刷新缓存，  
主要以XML配置的形式实现Spring容器的管理操作，不兼容Redis集群宕机或其他原因无法连接Redis集群时的情况，  
如果Redis集群宕机或其他原因无法连接Redis集群时，抛出异常，退出执行。

### mykit-cache-redis-spring-test


## mykit-cache-ehcache
mykit-cache架构下与ehcache缓存相关的组件

## mykit-cache-ehcache-spring
mykit-cache-ehcache 下主要与 Spring 整合Ehcache操作相关的组件，支持通过注解设置缓存有效时间

## mykit-cache-test
mykit-cache-redis-spring-xml的测试模块，对mykit-cache-redis-spring-xml提供单元测试用例,  
测试的入口为io.mykit.cache.test.redis.spring.test.xml.RedisTest,  
执行测试方法前需要先根据自身的Redis集群情况配置classpath:properties/redis.properties文件，  
将redis.properties中的Redis集群的节点IP和端口修改为自身的Redis集群节点的IP和端口

## mykit-cache-test-annotation
mykit-cache-redis-spring-annotation的测试模块，对mykit-cache-redis-spring-annotation提供单元测试用例，  
测试入口为：io.mykit.cache.test.redis.spring.test.annotation.TestRedisConfig,  
执行测试方法前需要先根据自身的Redis集群情况配置classpath:properties/redis.properties文件，    
将redis.properties中的Redis集群的节点IP和端口修改为自身的Redis集群节点的IP和端口

# 使用方法
1、需要使用Spring+Redis集群配置缓存：  
1) 需要兼容Redis集群宕机或其他原因无法连接Redis集群时的情况：  
在Maven的pom.xml中加入如下配置即可：  

        <dependency>
            <groupId>io.mykit.cache</groupId>
            <artifactId>mykit-cache-redis-spring-annotation</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>

此时，还需要根据具体情况在自身项目的合适模块中创建Redis的配置类，主要的功能为提供以Java注解的形式配置Spring和Redis集群整合的Spring容器管理，  
示例程序为：mykit-cache-test-annotation测试模块中的io.mykit.cache.test.redis.spring.config.AnnotationConfig类。  
```
package io.mykit.cache.test.redis.spring.annotation.config;

import io.mykit.cache.redis.spring.annotation.config.CacheRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/21 21:38
 * @description 提供以Java注解的形式配置Spring和Redis集群整合的Spring容器管理
 */
@Configuration
@EnableCaching
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(value = {"io.mykit.cache"})
@PropertySource(value = {"classpath:properties/redis-default.properties", "classpath:properties/redis.properties"})
public class AnnotationConfig extends CacheRedisConfig {
}

```

2) 不需要兼容Redis集群宕机或其他原因无法连接Redis集群时的情况：  
在Maven的pom.xml中加入如下配置即可：  

        <dependency>
            <groupId>io.mykit.cache</groupId>
            <artifactId>mykit-cache-redis-spring-xml</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>

此时还需要根据具体情况在自身项目的spring配置文件中，进行相关的配置，主要的配置项有：开启Spring注解扫描及代理，扫描的基本类中加入io.mykit.cache包，并按照顺序加载  
classpath*:properties/redis-default.properties, classpath*:properties/redis.properties文件，具体实例为：mykit-cache-test下的classpath:spring/spring-context.xml  
配置文件：  
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xmlns:p="http://www.springframework.org/schema/p"
	   xmlns:aop="http://www.springframework.org/schema/aop"
	   xmlns:context="http://www.springframework.org/schema/context"
	   xmlns:cache="http://www.springframework.org/schema/cache"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans
                        http://www.springframework.org/schema/beans/spring-beans-4.2.xsd
                        http://www.springframework.org/schema/context
                        http://www.springframework.org/schema/context/spring-context-4.2.xsd
                        http://www.springframework.org/schema/aop
                        http://www.springframework.org/schema/aop/spring-aop-4.2.xsd
                        http://www.springframework.org/schema/cache
                        http://www.springframework.org/schema/cache/spring-cache-4.2.xsd">

	<context:annotation-config />
	<aop:aspectj-autoproxy/>
	<context:component-scan base-package="io.mykit.cache"/>
	   <!-- 引入配置文件 -->
	  <context:property-placeholder location="classpath*:properties/redis-default.properties, classpath*:properties/redis.properties" system-properties-mode="FALLBACK"/>
	  <context:annotation-config />
	  <context:component-scan base-package="io.mykit.cache" />
 	 <import resource="classpath:redis/spring-redis.xml"/>
</beans>
```

注意：  
1、无论需不需要兼容Redis集群宕机或其他原因无法连接Redis集群时的情况，都需要在自身项目的classpath目录下创建redis.properties文件，配置自身Redis集群节点的IP和端口，  
目前，mykit-cache-redis-spring-annotation和mykit-cache-redis-spring-xml最多支持7台Redis集群，可根据自身实际情况扩展；  
如果自身的Redis集群不足7台，可在redis.properties文件中配置重复的Redis集群节点的IP和端口；      
  
2、在自身项目的classpath目录下创建redis.properties文件,此文件名不是强制要求的，可使用其他的文件名代替，但是此文件名必须和spring配置文件中加载的redis配置文件名以及  
以Java注解形式管理Spring容器的配置类中加载的redis配置文件名保持一致；  
  
3、自定义的Redis集群配置文件中的集群节点IP和端口的配置项名称必须和实例classpath:properties/redis.properties配置文件中的集群节点IP和端口的配置项名称相同；  
  
4、配置实例：  
比如，在我自己项目中的classpath:properties下redis集群的配置文件为redis.properties，具体内容如下：  
```
#redis cluster config
redis.cluster.defaultExpirationKey=defaultExpirationKey
redis.cluster.expirationSecondTime=300000
redis.cluster.preloadSecondTime=280000

#node info
redis.cluster.node.one=10.2.2.231
redis.cluster.node.one.port=7001

redis.cluster.node.two=10.2.2.231
redis.cluster.node.two.port=7002

redis.cluster.node.three=10.2.2.231
redis.cluster.node.three.port=7003

redis.cluster.node.four=10.2.2.231
redis.cluster.node.four.port=7004

redis.cluster.node.five=10.2.2.231
redis.cluster.node.five.port=7005

redis.cluster.node.six=10.2.2.231
redis.cluster.node.six.port=7006

redis.cluster.node.seven=10.2.2.231
redis.cluster.node.seven.port=7006
```
则在我项目的spring配置文件中需要加载的配置文件为：
```
<context:property-placeholder location="classpath*:properties/redis-default.properties, classpath*:properties/redis.properties" system-properties-mode="FALLBACK"/>
```

或者在我项目的配置类中需要加载的配置文件注解为：
```
@PropertySource(value = {"classpath:properties/redis-default.properties", "classpath:properties/redis.properties"})
```

5、具体使用  
1) 在相关的查询方法上加上无key属性的@Cacheable注解：
```
@Cacheable(value={"test#10#2"})
```
没有配置@Cacheable的key属性，此时的@Cacheable的key属性值按照一定策略自定生成，即以当前类名(完整包名+类名)+方法名+方法类型列表+方法参数列表的HashCode为当前@Cacheable的key属性。  
具体的key生成策略类为mykit-cache-redis-spring中的io.mykit.cache.redis.spring.cache.CacheKeyGenerator类；  

2) 在相关的查询方法上加上有key属性的@Cacheable注解
```
@Cacheable(value={"test#10#2"} key="key" + ".#defaultValue")
```
配置了@Cacheable的key属性，此时@Cacheable的key属性值为key拼接参数defaultValue的值的结果的HashCode值。

注意：  
1、@Cacheable注解中没有key属性，框架会为@Cacheable生成Key属性，也就是说key属性不是必须的； 
   
2、@Cacheable注解没有配置key属性，则以当前类名(完整包名+类名)+方法名+方法类型列表+方法参数列表的HashCode为当前@Cacheable的key属性；  
  
3、@Cacheable注解配置了key属性，则以当前key的HashCode作为当前@Cacheable的key属性；  
  
4、@Cacheable的value属性中我们配置的值为 test#10#2，此时表示@Cacheable的缓存名称为test，其中10表示缓存有效时长(单位为秒)，2表示距离缓存失效的剩余时长(单位为秒)，  
  
即@Cacheable的value属性配置格式为：缓存名称#expireTime#reloadTime，框架规定必须以#作为分隔符  
  
expireTime：表示缓存的有效时长，单位秒；  
reloadTime：表示距离缓存失效的剩余时长，单位秒；  
expireTime 需要大于 reloadTime，否则无意义  
  
5、@Cacheable的value属性说明  
  
以缓存名称#expireTime#reloadTime格式配置@Cacheable的value属性后，框架会将查询结果放到缓存中，有效时长为expireTime秒，距离缓存失效的剩余时长为reloadTime秒； 
   
当在将数据存入缓存时，经过了0秒——(expireTime-reloadTime)秒时间范围时，再次调用方法，则直接从缓存中获取数据；  
  
当在将数据存入缓存时，经过了reloadTime秒——expireTime秒时间范围时，再次调用方法，框架会通过代理和反射的方式主动调用原方法从真正的数据源获取数据后刷新缓存； 
  
当在将数据存入缓存时，经过了超过expireTime秒的时间，则缓存失效，再次调用方法，则执行原方法查询数据，框架会自动将查询结果存入缓存； 
  
当框架通过代理和反射的方式主动调用原方法从真正的数据源获取数据后刷新缓存时，为防止请求的多个线程同时执行刷新缓存的操作，框架提供了分布式锁来保证只有一个线程执行刷新缓存操作； 
  
框架主动调用原方法从真正的数据源获取数据后刷新缓存的操作与用户的请求操作是异步的，不会影响用户请求的性能； 
  
框架主动调用原方法从真正的数据源获取数据后刷新缓存的操作对用户请求透明，即用户感知不到框架主动刷新缓存的操作； 
  
其他：  

1)当 @Cacheable 的Value只配置了缓存名称，比如配置为@Cacheable(value="test")  
此时的expireTime默认为redis配置文件的redis.cluster.expirationSecondTime属性值，单位为秒；reloadTime默认为redis配置文件的redis.cluster.preloadSecondTime属性值，单位为秒；

属性值的加载顺序为：优先加载自定义的redis配置文件的redis.cluster.expirationSecondTime属性值和redis.cluster.preloadSecondTime属性值，如果自定义的redis配置文件无相关的属性值；  
则从框架默认的redis配置文件redis-default.properties文件中加载；  
  
2)当 @Cacheable 的Value配置缓存名称和失效时长，比如配置为@Cacheable(value="test#10")  
此时的reloadTime默认为redis配置文件的redis.cluster.preloadSecondTime属性值，单位为秒;  
  
属性值的加载顺序为：优先加载自定义的redis配置文件的redis.cluster.preloadSecondTime属性值，如果自定义的redis配置文件无相关的属性值；  
则从框架默认的redis配置文件redis-default.properties文件中加载；  

3) 当 @Cacheable 的Value配置缓存名称、失效时长和距离缓存失效的剩余时长，比如配置为：@Cacheable(value="test#10#2")    
此时不会加载默认的expireTime和reloadTime，框架会直接使用@Cacheable注解中value属性配置的expireTime和reloadTime；
  
4) 无论@Cacheable的Value属性是否配置了缓存时长信息，则都不会出现只配置reloadTime，没有配置expireTime的情况，框架规定的value属性格式为：缓存名称#expireTime#reloadTime  
即只会出现的格式为：  
  
缓存名称  
缓存名称#expireTime  
缓存名称#expireTime#reloadTime  
  
不会存在单独出现reloadTime的情况，会出现配置了缓存名称#expireTime，reloadTime使用配置文件默认的时长配置的情况；
  
  
# 备注
本项目还在开发中，目前未添加到Maven中央仓库，后续开发完成会添加到Maven中央仓库

# 注意事项
1、mykit-cache-redis-spring-xml引用和mykit-cache-redis-spring-annotation引用是互斥的，即在一个工程中mykit-cache-redis-spring-xml和mykit-cache-redis-spring-annotation只能同时引用一个；  
  
2、mykit-cache-redis-spring-xml和mykit-cache-redis-spring-annotation的功能是一样的，但是mykit-cache-redis-spring-annotation工程兼容Redis集群宕机或其他原因无法连接Redis集群时的情况；  
  
3、如果Redis集群宕机或其他原因无法连接Redis集群时，则mykit-cache-redis-spring-xml会抛出异常，退出执行；而mykit-cache-redis-spring-annotation则会打印相关的异常信息，继续向下执行原来的方法。  
  



