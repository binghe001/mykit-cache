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
mykit-cache-redis 下主要与 Spring 整合 Redis操作相关的组件，支持通过注解设置缓存有效时间和主动刷新缓存

## mykit-cache-ehcache
mykit-cache架构下与ehcache缓存相关的组件

## mykit-cache-ehcache-spring
mykit-cache-ehcache 下主要与 Spring 整合Ehcache操作相关的组件，支持通过注解设置缓存有效时间

## mykit-cache-test
整个项目的测试模块，对整个项目提供单元测试用例

# 使用方法
在Maven的pom.xml中加入如下配置即可：  

        <dependency>  
           <groupId>io.mykit.cache</groupId>  
            <artifactId>mykit-cache</artifactId>  
            <version>1.0.0</version>  
        </dependency>


# 备注
本项目还在开发中，目前未添加到Maven中央仓库，后续开发完成会添加到Maven中央仓库


