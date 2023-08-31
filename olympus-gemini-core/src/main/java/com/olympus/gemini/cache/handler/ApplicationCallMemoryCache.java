package com.olympus.gemini.cache.handler;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.olympus.gemini.cache.data.CacheableData;
import com.olympus.gemini.utils.UncheckCastUtil;
import org.checkerframework.checker.index.qual.NonNegative;

import java.util.Objects;

/**
 * 应用调用本地缓存
 *
 * @author eddie.lys
 * @since 2023/8/28
 */
public class ApplicationCallMemoryCache {

    private static final ApplicationCallMemoryCache INSTANCE = new ApplicationCallMemoryCache();

    /**
     * Cache<参数MD5, method返回值>
     */
    private static final Cache<String, CacheableData> CALL_MESSAGE_CACHE = Caffeine.newBuilder()
            .expireAfter(new Expiry<String, CacheableData>() {
                @Override
                public long expireAfterCreate(String key, CacheableData cacheableData, long currentTime) {
                    return cacheableData.getTimeUnit().toNanos(cacheableData.getExpireAfterCreate());
                }

                @Override
                public long expireAfterUpdate(String key, CacheableData cacheableData, long currentTime, @NonNegative long currentDuration) {
                    return cacheableData.getTimeUnit().toNanos(cacheableData.getExpireAfterCreate());
                }

                @Override
                public long expireAfterRead(String key, CacheableData cacheableData, long currentTime, @NonNegative long currentDuration) {
                    return currentDuration;
                }
            })
            .build();

    public void putVal(String callMessageMd5, CacheableData cacheableData) {
        CALL_MESSAGE_CACHE.put(callMessageMd5, cacheableData);
    }

    public <T> T getVal(String callMessageMd5) {
        CacheableData cacheableData = CALL_MESSAGE_CACHE.getIfPresent(callMessageMd5);
        if (Objects.isNull(cacheableData)) {
            return null;
        }
        return UncheckCastUtil.castUncheckedObject(cacheableData.getResultValue());
    }

    public static ApplicationCallMemoryCache getInstance() {
        return ApplicationCallMemoryCache.INSTANCE;
    }
}
