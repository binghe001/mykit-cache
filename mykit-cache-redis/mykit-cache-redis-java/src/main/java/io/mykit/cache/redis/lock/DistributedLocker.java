package io.mykit.cache.redis.lock;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;

/**
 * @author binghe
 * @version 1.0.0
 * @description 分布式锁接口
 */
public interface DistributedLocker {
	
	/**
	 * 根据lockKey加锁，拿不到锁就一直阻塞
	 * @param lockKey 加锁的key
	 * @return RLock对象
	 */
	RLock lock(String lockKey);
	 
    /**
     * 根据lockKey加锁，当时间超过timeout秒时锁自动释放
     * @param lockKey 加锁的key
     * @param leaseTime 锁超时时间，默认为秒
     * @return RLock对象
     */
    RLock lock(String lockKey, long leaseTime);
 
    /**
     * 根据lockKey加锁，当时间超过timeout时锁自动释放
     * @param lockKey 加锁的key
     * @param leaseTime 锁超时时间
     * @param unit 时间单位
     * @return RLock对象
     */
    RLock lock(String lockKey, long leaseTime, TimeUnit unit);
 
    /**
     * 尝试获取锁，拿到锁返回true，否则返回false
     * @param lockKey 加锁的key
     * @param waitTime 加锁等待时间
     * @param leaseTime 加锁超时时间
     * @param unit 时间单位
     * @return true:加锁成功; false:加锁失败
     */
    boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit);
    
    /**
     * 尝试获取锁，拿到锁返回true，否则返回false
     * @param lockKey 加锁的key
     * @param waitTime 加锁等待时间
     * @param leaseTime 加锁超时时间
     * @return true:加锁成功; false:加锁失败
     */
    boolean tryLock(String lockKey, long waitTime, long leaseTime);
    
    /**
     * 尝试加锁，拿到锁返回true，否则返回false
     * @param lockKey 加锁的key
     * @return true:加锁成功; false:加锁失败
     */
    boolean tryLock(String lockKey);
    
    /**
     * 尝试加锁，拿到锁返回true，否则返回false
     * @param lockKey 加锁的key
     * @param time 加锁时间，默认为秒
     * @return true:加锁成功; false:加锁失败
     */
    boolean tryLock(String lockKey, long time);
    
    /**
     * 尝试加锁，拿到锁返回true，否则返回false
     * @param lockKey 加锁的key
     * @param time 加锁时间
     * @param unit 时间单位
     * @return true:加锁成功; false:加锁失败
     */
    boolean tryLock(String lockKey, long time, TimeUnit unit);
 
    /**
     * 根据加锁的key释放锁
     * @param lockKey 加锁的key
     */
    void unlock(String lockKey);
 
    /**
     * 根据RLock对象解锁
     * @param lock RLock对象
     */
    void unlock(RLock lock);

}
