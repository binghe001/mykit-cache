package io.mykit.cache.redis.spring.annotation.serializer;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2018/8/3 09:53
 * @description 必须重写序列化器，否则@Cacheable注解的key会报类型转换错误
 */

public class StringRedisAnnotationSerializer implements RedisSerializer<Object> {
    private final Charset charset;

    private final String target = "\"";

    private final String replacement = "";

    public StringRedisAnnotationSerializer() {
        this(Charset.forName("UTF8"));
    }

    public StringRedisAnnotationSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public String deserialize(byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }

    @Override
    public byte[] serialize(Object object) {
        String string = JSON.toJSONString(object);
        if (string == null) {
            return null;
        }
        string = string.replace(target, replacement);
        return string.getBytes(charset);
    }
}
