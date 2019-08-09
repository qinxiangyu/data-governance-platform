package com.qinxy.service;

import java.util.concurrent.TimeUnit;

/**
 * Created by qinxy on 2019/4/2.
 */
public interface RedisService {

        String redis(String key);

        boolean hasKey(String key);

        void redis(String key, String value);

        Boolean redisIfAbsent(String key, String value);

        void redis(String key, String value, Long timeout, TimeUnit timeUnit);

        boolean expire(String key, Long timeout, TimeUnit timeUnit);

        Long getExpire(String key, TimeUnit timeUnit);

        void delete(String key);

        Long increment(String key, Long delta);

        Long leftPush(String key, String value);

        String rightPop(String key);

        Long listSize(String key);
}
