package io.mykit.cache.test.redis.spring.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * 加载文件
 * @author binghe
 *
 */
public class LoadFile {
	
	private static final Logger logger = LoggerFactory.getLogger(LoadFile.class);
	
	private static Properties properties = null;
	static{
		try {
			InputStream in = LoadFile.class.getResourceAsStream("/test_redis.properties");
			properties = new Properties();
			properties.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getValue(String key){
		logger.info("从文件获取数据，传入的key是：" + key);
		return properties.getProperty(key, "");
	}
}
