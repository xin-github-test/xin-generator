package com.xin.web.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 缓存管理
 */
public class CacheManager {
    //redis
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    //本地缓存 caffeine
    Cache<String,Object> localCache = Caffeine.newBuilder()
            .expireAfterWrite(100, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    /**
     * 写入缓存
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        //写入本地缓存
        localCache.put(key, value);
        //写入redis,并设置过期时间
        redisTemplate.opsForValue().set(key, value, 100, TimeUnit.MINUTES);
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public Object get(String key) {
        //先从本地缓存中获取
        Object value = localCache.getIfPresent(key);
        if (value != null) {
            return value;
        }
        //本地缓存未命中，从redis中获取
        value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            //存入本地缓存
            localCache.put(key, value);
        }
        return value;
    }

    /**
     * 移除缓存
     * @param key
     */
    public void delete(String key) {
        localCache.invalidate(key);
        redisTemplate.delete(key);
    }
}
