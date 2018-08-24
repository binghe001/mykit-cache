# 框架此模块暂时不做实现，由于Spring与Ehcache的整合过于简单，可自行实现Spring与Ehcache的整合，这个不提供封装了。
# spring4配置基于注解的ehcache缓存
## 一、 ehcache配置文件ehcache.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<ehcache name="es">
    <diskStore path="java.io.tmpdir"/>
    <defaultCache
        maxElementsInMemory="10000" 
		eternal="false"
		timeToIdleSeconds="30" 
		timeToLiveSeconds="30" 
		overflowToDisk="true">
    </defaultCache>
 
 <!-- 配置自定义缓存 
	maxElementsInMemory：缓存中允许创建的最大对象数 
	eternal：缓存中对象是否为永久的，如果是，超时设置将被忽略，对象从不过期。 
	timeToIdleSeconds：缓存数据的钝化时间，也就是在一个元素消亡之前， 两次访问时间的最大时间间隔值，这只能在元素不是永久驻留时有效， 
	如果该值是 0 就意味着元素可以停顿无穷长的时间。
	timeToLiveSeconds：缓存数据的生存时间，也就是一个元素从构建到消亡的最大时间间隔值， 
	这只能在元素不是永久驻留时有效，如果该值是0就意味着元素可以停顿无穷长的时间。 
	overflowToDisk：内存不足时，是否启用磁盘缓存。 
	memoryStoreEvictionPolicy：缓存满了之后的淘汰算法。 
-->

<cache name="statisticServiceCache" 
	maxElementsInMemory="1000"
	eternal="false" 
	overflowToDisk="true" 
	timeToIdleSeconds="900"
	timeToLiveSeconds="1800" 
	diskPersistent="false"
	memoryStoreEvictionPolicy="LFU" />
</ehcache>
```
## 二、 spring-cache注解及ehcache bean配置
```
<cache:annotation-driven cache-manager="cacheManager"/>
<!-- cacheManager工厂类，指定ehcache.xml的位置 -->
<bean id="ehcache" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean">
	<property name="configLocation"  value="/WEB-INF/ehcache.xml"/>
	<!-- <property name="shared"  value="true"/>   -->  
</bean>
<!-- 声明cacheManager -->
<bean id="cacheManager" class="org.springframework.cache.ehcache.EhCacheCacheManager">
	<property name="cacheManager" ref="ehcache" />
</bean>
```
## 三、 确认开启了spring的aop支持
```
<aop:aspectj-autoproxy/>
```
## 四、 Spring的cache注解的使用
### 1. @Cacheable
@Cacheable 主要的参数
	 

value
	

缓存的名称，在 spring 配置文件中定义，必须指定至少一个
	

例如：
@Cacheable(value=”mycache”) 或者 
@Cacheable(value={”cache1”,”cache2”}

key
	

缓存的 key，可以为空，如果指定要按照 SpEL 表达式编写，如果不指定，则缺省按照方法的所有参数进行组合
	

例如：
@Cacheable(value=”testcache”,key=”#userName”)

condition
	

缓存的条件，可以为空，使用 SpEL 编写，返回 true 或者 false，只有为 true 才进行缓存
	

例如：
@Cacheable(value=”testcache”,condition=”#userName.length()

如下示例：
```
@Cacheable(value = "statisticServiceCache", key = "'activityChartData_' + #urlID")
public ResultInfo getActivityChartData(String urlID, Date startMonth,Date endMonth) {
    …
}
```
这个注解用于，在调用被注解的方法时，首先检查当前缓存系统中是否存在键值为key的缓存。如果存在，则直接返回缓存对象，不执行该方法。如果不存在，则调用该方法，并将得到的返回值写入缓存中。
### 2. @CachePut
@CachePut 主要的参数
	 

value
	

缓存的名称，在 spring 配置文件中定义，必须指定至少一个
	

例如：
@Cacheable(value=”mycache”) 或者 
@Cacheable(value={”cache1”,”cache2”}

key
	

缓存的 key，可以为空，如果指定要按照 SpEL 表达式编写，如果不指定，则缺省按照方法的所有参数进行组合
	

例如：
@Cacheable(value=”testcache”,key=”#userName”)

condition
	

缓存的条件，可以为空，使用 SpEL 编写，返回 true 或者 false，只有为 true 才进行缓存
	

例如：
@Cacheable(value=”testcache”,condition=”#userName.leng
@CachePut用于写入缓存，但是与@ Cacheable不同，@CachePut注解的方法始终执行，然后将方法的返回值写入缓存，此注解主要用于新增或更新缓存。
### 3. @CacheEvict
@CacheEvict 主要的参数
	 

value
	

缓存的名称，在 spring 配置文件中定义，必须指定至少一个
	

例如：
@CachEvict(value=”mycache”) 或者 
@CachEvict(value={”cache1”,”cache2”}

key
	

缓存的 key，可以为空，如果指定要按照 SpEL 表达式编写，如果不指定，则缺省按照方法的所有参数进行组合
	

例如：
@CachEvict(value=”testcache”,key=”#userName”)

condition
	

缓存的条件，可以为空，使用 SpEL 编写，返回 true 或者 false，只有为 true 才清空缓存
	

例如：
@CachEvict(value=”testcache”,
condition=”#userName.length()>2”)

allEntries
	

是否清空所有缓存内容，缺省为 false，如果指定为 true，则方法调用后将立即清空所有缓存
	

例如：
@CachEvict(value=”testcache”,allEntries=true)

beforeInvocation
	

是否在方法执行前就清空，缺省为 false，如果指定为 true，则在方法还没有执行的时候就清空缓存，缺省情况下，如果方法执行抛出异常，则不会清空缓存
	

例如：
@CachEvict(value=”testcache”，beforeInvocation=true)

@CacheEvict用于删除缓存

