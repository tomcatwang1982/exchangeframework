package com.tomcatwang.redis.config;

import com.tomcatwang.redis.service.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    private static Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${redis.time.tolive}")
    private long redisTimeTolive = 0;

    @Value("${redis.subscribe.info}")
    private String redisSubscribeInfo;

    //过期时间 毫秒
    private Duration timeToLive = Duration.of(100000, ChronoUnit.MILLIS);

    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    /**
     * 缓存管理器.
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(this.timeToLive)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer(Object.class)))
                .disableCachingNullValues();

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();

        return redisCacheManager;
    }

    /*
    spring-data-redis  版本不同，方法也不一样
//    上面是1.5
    下面是2.0
    */

//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//        RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory).build();
//        return re     disCacheManager;
//    }


    /**
     * @return 返回类型
     * @Description: 防止redis入库序列化乱码的问题
     */
    @Bean("redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        try {
            RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
            redisTemplate.setConnectionFactory(redisConnectionFactory);
            redisTemplate.setKeySerializer(new StringRedisSerializer());//key序列化
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Object.class));  //value序列化

            redisTemplate.setHashKeySerializer(new StringRedisSerializer());
            redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());

            redisTemplate.afterPropertiesSet();
            return redisTemplate;
        } catch (Exception e) {
            //todo 输出日志
            LOGGER.info("redis template 获取失败,请检查问题!!!", e.getMessage(), e.getStackTrace());
            return null;
        }
    }


    /**
     * @参数
     * @返回值
     * @创建人 cx
     * @创建时间
     * @描述 //初始化监听器
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) throws Exception {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        if (null != redisSubscribeInfo && !redisSubscribeInfo.equalsIgnoreCase("")) {
            String[] infos = redisSubscribeInfo.split(",");
            if (null != infos && infos.length > 0) {
                for (String info : infos) {
                    String classZ = info.split("\\|")[0];
                    String method = info.split("\\|")[1];
                    String topic = info.split("\\|")[2];
                    IRedisService service = (IRedisService) Class.forName(classZ).newInstance();
                    MessageListenerAdapter listenerAdapter = new MessageListenerAdapter("", method);
                    container.addMessageListener(listenerAdapter, new PatternTopic(topic));// 通道的名字
                    LOGGER.info("初始化监听成功，监听通道：【" + topic + "】");
                }
            }
        }
        //配置监听通道
        return container;
    }


}
