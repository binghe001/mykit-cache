package io.mykit.cache.test.redis.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author binghe
 * @version 1.0.0
 * @description 测试实体
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Person implements Serializable {
    private static final long serialVersionUID = 1609839334166963973L;

    private String name;

    private Integer age;
}
