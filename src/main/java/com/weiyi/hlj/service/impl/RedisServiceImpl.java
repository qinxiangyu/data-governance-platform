package com.weiyi.hlj.service.impl;

import com.weiyi.hlj.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by qinxy on 2019/4/2.
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${spring.redis.namespace:data-governance-platform:}")
    protected String redisNamespace;

    @Override
    public String redis(String key) {
        return redisTemplate.opsForValue().get(key(key));
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key(key));
    }

    @Override
    public void redis(String key, String value) {
        redisTemplate.opsForValue().set(key(key),value);
    }

    @Override
    public Boolean redisIfAbsent(String key, String value) {
        return redisTemplate.opsForValue().setIfAbsent(key(key),value);
    }

    @Override
    public void redis(String key, String value, Long timeout, TimeUnit timeUnit) {
        redis(key, value);
        expire(key,timeout,timeUnit);
    }

    @Override
    public boolean expire(String key, Long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key(key),timeout,timeUnit);
    }

    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key(key),timeUnit);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key(key));
    }

    @Override
    public Long increment(String key, Long delta) {
        return redisTemplate.opsForValue().increment(key(key),delta);
    }

    @Override
    public Long leftPush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key(key),value);
    }

    @Override
    public String rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    @Override
    public Long listSize(String key) {
        return redisTemplate.opsForList().size(key(key));
    }

    private String key(String key){
        return redisNamespace.concat(key);
    }
}
