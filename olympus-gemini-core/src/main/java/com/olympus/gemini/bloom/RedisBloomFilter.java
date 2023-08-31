package com.olympus.gemini.bloom;

import com.google.common.base.Preconditions;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

/**
 * @author eddie.lys
 * @since 2023/8/30
 */
public class RedisBloomFilter<T> {

    private final RedisTemplate<String, Object> redisTemplate;

    private final BloomFilterStrategy<T> bloomFilterHelper;

    public RedisBloomFilter(RedisTemplate<String, Object> redisTemplate, BloomFilterStrategy<T> bloomFilterHelper) {
        this.redisTemplate = redisTemplate;
        this.bloomFilterHelper = bloomFilterHelper;
    }

    /**
     * 根据给定的布隆过滤器添加值
     */
    public void addByBloomFilter(String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            redisTemplate.opsForValue().setBit(key, i, true);
        }
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     */
    public boolean mightContain(String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            Boolean bit = redisTemplate.opsForValue().getBit(key, i);;
            if (Objects.isNull(bit) || !bit) {
                return false;
            }
        }
        return true;
    }
}
