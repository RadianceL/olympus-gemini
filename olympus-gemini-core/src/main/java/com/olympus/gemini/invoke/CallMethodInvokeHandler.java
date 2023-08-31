package com.olympus.gemini.invoke;

import com.olympus.gemini.cache.data.CacheableData;
import com.olympus.gemini.cache.handler.RedisCallMemoryCache;
import com.olympus.gemini.invoke.annotation.GeminiIdempotent;
import com.olympus.gemini.selector.ParameterSelector;
import com.olympus.gemini.utils.UncheckCastUtil;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 动态代理实际工作对象
 * @author eddie.lys
 * @since 2023/8/28
 */
public class CallMethodInvokeHandler implements MethodInterceptor {

    private final RedisCallMemoryCache redisCallMemoryCache;

    public CallMethodInvokeHandler(RedisCallMemoryCache redisCallMemoryCache) {
        this.redisCallMemoryCache = redisCallMemoryCache;
    }

    public <T> T createCallMethodInvokeHandler(Object target) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return UncheckCastUtil.castUncheckedObject(enhancer.create());
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        GeminiIdempotent geminiIdempotent = method.getAnnotation(GeminiIdempotent.class);
        if (Objects.isNull(geminiIdempotent)) {
            return methodProxy.invokeSuper(obj, args);
        }
        String methodName = method.getName();
        if (StringUtils.isNotBlank(geminiIdempotent.methodName())) {
            methodName = geminiIdempotent.methodName();
        }
        String uniqueFromParameterId = ParameterSelector.getUniqueFromParameter(methodName, args);
        // 在方法调用前可以插入前置逻辑
        Object val = redisCallMemoryCache.getVal(uniqueFromParameterId);
        if (Objects.nonNull(val)) {
            return val;
        }
        // 调用目标方法
        Object result = methodProxy.invokeSuper(obj, args);
        CacheableData cacheableData = CacheableData.builder()
                .expireAfterCreate(geminiIdempotent.duration())
                .timeUnit(geminiIdempotent.timeUnit())
                .resultValue(result)
                .build();
        redisCallMemoryCache.putVal(uniqueFromParameterId, cacheableData);
        // 在方法调用后可以插入后置逻辑
        return result;
    }
}
