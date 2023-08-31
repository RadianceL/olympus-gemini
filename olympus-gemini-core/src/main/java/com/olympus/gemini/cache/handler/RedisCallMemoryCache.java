package com.olympus.gemini.cache.handler;

import com.olympus.gemini.cache.data.CacheableData;
import com.olympus.gemini.utils.UncheckCastUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Objects;

/**
 * 集群调用缓存对象
 *
 * @author eddie.lys
 * @since 2023/8/28
 */
public class RedisCallMemoryCache {
    private final  ValueOperations<String, CacheableData> valueOperations;

    public RedisCallMemoryCache(RedisTemplate<String, CacheableData> redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void putVal(String callMessageMd5, CacheableData cacheableData) {
        valueOperations.set(callMessageMd5, cacheableData, cacheableData.getExpireAfterCreate(), cacheableData.getTimeUnit());
    }

    public <T> T getVal(String callMessageMd5) {
        CacheableData cacheableData = valueOperations.get(callMessageMd5);
        if (Objects.isNull(cacheableData)) {
            return null;
        }
        return UncheckCastUtil.castUncheckedObject(cacheableData.getResultValue());
    }

}
