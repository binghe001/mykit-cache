package io.mykit.cache.redis.spring.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.cache.support.NullValue;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/3 12:46
 * @description 对象序列化
 */
public class ObjectFastJsonRedisSerializer extends FastJsonRedisSerializer<Object>{
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<Object> clazz;

    public ObjectFastJsonRedisSerializer(Class<Object> clazz) {
        super(clazz);
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(Object t) throws SerializationException {
        if (t == null || t instanceof NullValue) {
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        return JSON.parseObject(str, clazz);
    }
}
