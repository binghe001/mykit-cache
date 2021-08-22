package io.mykit.cache.redis.lock.impl;

import io.mykit.cache.redis.lock.DistributedLocker;
import io.mykit.cache.redis.lock.builder.RedissonBuilder;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class RedissonDistributedLocker implements DistributedLocker {
	
	private static final String REDISSON_DISTRIBUTED_LOCK_PREFIX = "redisson_distributed_lock_prefix_";
	
	private final Logger logger = LoggerFactory.getLogger(RedissonDistributedLocker.class);
	
	@Override
	public RLock lock(String lockKey) {
		RLock lock = this.getRLock(lockKey);
		lock.lock();
		return lock;
	}

	@Override
	public RLock lock(String lockKey, long leaseTime) {
		RLock lock = this.getRLock(lockKey);
		lock.lock(leaseTime, TimeUnit.SECONDS);
		return lock;
	}

	@Override
	public RLock lock(String lockKey, long leaseTime, TimeUnit unit) {
		RLock lock = this.getRLock(lockKey);
		lock.lock(leaseTime, unit);
		return lock;
	}

	@Override
	public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
		RLock lock = this.getRLock(lockKey);
		try {
			return lock.tryLock(waitTime, leaseTime, unit);
		} catch (InterruptedException e) {
			logger.error("尝试获取分布式锁异常: {}", e);
			return false;
		}
	}
	
	@Override
	public boolean tryLock(String lockKey, long waitTime, long leaseTime) {
		RLock lock = this.getRLock(lockKey);
		try {
			return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.error("尝试获取分布式锁异常: {}", e);
			return false;
		}
	}
	
	@Override
	public boolean tryLock(String lockKey) {
		RLock lock = this.getRLock(lockKey);
		return lock.tryLock();
	}
	
	@Override
	public  boolean tryLock(String lockKey, long time) {
		RLock lock = this.getRLock(lockKey);
		try {
			return lock.tryLock(time, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.error("尝试获取分布式锁异常: {}", e);
			return false;
		}
	}
	
	@Override
	public  boolean tryLock(String lockKey, long time, TimeUnit unit) {
		RLock lock = this.getRLock(lockKey);
		try {
			return lock.tryLock(time, unit);
		} catch (InterruptedException e) {
			logger.error("尝试获取分布式锁异常: {}", e);
			return false;
		}
	}

	@Override
	public void unlock(String lockKey) {
		RLock lock = this.getRLock(lockKey);
		lock.unlock();
	}

	@Override
	public void unlock(RLock lock) {
		lock.unlock();
	}
	
	/**
	 * 获取RLock句柄
	 * @param lockKey 传递进来的lockKey
	 * @return RLock对象
	 */
	private RLock getRLock(String lockKey) {
		return RedissonBuilder.getInstance().getLock(this.getLockKey(lockKey));
	}
	
	
	/**
	 * 获取lockKey，规则是在传递的lockKey前面加上redisson_distributed_lock_prefix_ 前缀
	 * @param lockKey 传递进来的lockKey
	 * @return 拼接后的key
	 */
	private String getLockKey(String lockKey) {
		return REDISSON_DISTRIBUTED_LOCK_PREFIX.concat(lockKey);
	}

}
