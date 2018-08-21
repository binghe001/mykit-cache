package io.mykit.cache.redis.spring.annotation.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.cache.support.NullValue;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/3 09:52
 * @description FastJson序列化
 */

public class FastJsonRedisAnnotationSerializer<T> implements RedisSerializer<T> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<T> clazz;

    public FastJsonRedisAnnotationSerializer(){
        super();
    }

    public FastJsonRedisAnnotationSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null || t instanceof NullValue) {
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        return (T) JSON.parseObject(str, clazz);
    }
}
