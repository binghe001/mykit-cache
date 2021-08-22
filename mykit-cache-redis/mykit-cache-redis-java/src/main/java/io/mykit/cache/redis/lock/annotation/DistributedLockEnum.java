package io.mykit.cache.redis.lock.annotation;

/**
 * @author binghe
 * @version 1.0.0
 * @description 枚举，标识使用lock还是tryLock
 */
public enum DistributedLockEnum {
	
	/**
	 * LOCK模式
	 */
	LOCK(1, "LOCK模式"),
	/**
	 * TRYLOCK模式
	 */
	TRYLOCK(2, "TRYLOCK模式");

	private final Integer code;
	private final String desc;
	
	DistributedLockEnum(final Integer code, final String desc){
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
}
