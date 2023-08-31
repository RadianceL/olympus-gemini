package com.olympus.gemini.cache.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 可缓存对象
 *
 * @author eddie.lys
 * @since 2023/8/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheableData implements Serializable {
    /**
     * 过期时间
     */
    private Long expireAfterCreate;
    /**
     * 过期时间单位
     */
    private TimeUnit timeUnit;
    /**
     * CallMethod 返回对象
     */
    private Object resultValue;
}
