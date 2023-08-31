package com.olympus.gemini.invoke.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 需要幂等的方法
 * @author eddie.lys
 * @since 2023/8/31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GeminiIdempotent {

    /**
     * 幂等方法自定义名称
     * 用于识别方法名称（注：使用该名称则URL以及实际的方法名会被忽略）
     */
    String methodName() default "";
    /**
     * 用于幂等判断的入参将仅针对recognizeKey生效
     */
    String recognizeKey() default "";
    /**
     * 持续时长
     */
    long duration() default 1L;
    /**
     * 持续时间的单位 默认秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

}
