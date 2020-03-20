package io.mykit.cache.redis.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: liuyazhuang
 * @Date: 2018/7/18 15:14
 * @Description: 加载指定的redis.properties
 */

public class LoadRedisProp extends BaseRedisProp {

    private volatile static Properties instance;

    static {
        InputStream in = LoadRedisProp.class.getClassLoader().getResourceAsStream(FILE_NAME);
        instance = new Properties();
        try {
            instance.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getStringValue(String key){
        if(instance == null) return "";
        return instance.getProperty(key, "");
    }

    public static Integer getIntegerValue(String key){
       String v = getStringValue(key);
       return (v == null || "".equals(v.trim())) ? 0 : Integer.parseInt(v);
    }

    public static Boolean getBooleanValue(String key){
       String v = getStringValue(key);
       return (v == null || "".equals(v.trim())) ? false : Boolean.parseBoolean(key);
    }
}
