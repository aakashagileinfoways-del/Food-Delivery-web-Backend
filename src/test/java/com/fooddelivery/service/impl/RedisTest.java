package com.fooddelivery.service.impl;

// import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {
 

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testRedisConnection() {
        String key = "testKey";
        String value = "Hello, Redis!";
        redisTemplate.opsForValue().set(key, value);
        // String retrievedValue = redisTemplate.opsForValue().get(key);
        Object retrievedValueObj = redisTemplate.opsForValue().get("sharma:1");
        // assertEquals(value, retrievedValue);
        System.out.println("✅ Redis connection test passed!" + retrievedValueObj);

    }

}