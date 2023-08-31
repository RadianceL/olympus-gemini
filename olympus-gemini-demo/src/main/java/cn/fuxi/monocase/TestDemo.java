package cn.fuxi.monocase;

import com.olympus.gemini.cache.data.CacheableData;
import com.olympus.gemini.cache.handler.RedisCallMemoryCache;
import com.olympus.gemini.invoke.CallMethodInvokeHandler;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;

/**
 * @author eddie.lys
 * @since 2023/8/30
 */
public class TestDemo {

    public static void main(String[] args) throws InterruptedException {

        RedisTemplate<String, CacheableData> redisTemplate = createRedisTemplate();
        CallMethodInvokeHandler callMethodInvokeHandler = new CallMethodInvokeHandler(new RedisCallMemoryCache(redisTemplate));
        DemoService demoService = callMethodInvokeHandler.createCallMethodInvokeHandler(new DemoService());

        for (int i = 0; i < 10; i++) {
            System.out.println(demoService.test("1", "2"));
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public static RedisTemplate<String, CacheableData> createRedisTemplate() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("localhost");
        redisStandaloneConfiguration.setPort(6379);
        return getStringCacheableDataRedisTemplate(redisStandaloneConfiguration);
    }

    private static RedisTemplate<String, CacheableData> getStringCacheableDataRedisTemplate(RedisStandaloneConfiguration redisStandaloneConfiguration) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        jedisConnectionFactory.afterPropertiesSet();

        RedisTemplate<String, CacheableData> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);

        // Configure serializers
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
