package com.minlingchao.spring.boot.cache.starter.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minlingchao.spring.boot.cache.starter.log.LogTracerCacheErrorHandler;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * 如果只是简单的使用Spring Cache，可以继承该类，实现redisTemplate()，并添加@Bean即可 如果需要单个服务内，不同业务使用不同的NCR实例，则需自行override
 * cacheManager()方法
 */
public abstract class RedisCachingConfigurerSupport extends CachingConfigurerSupport {

  @Bean
  public abstract RedisTemplate<String, Object> redisTemplate();

  @Bean
  @Override
  @Primary
  public CacheManager cacheManager() {
    RedisCacheManager manager = new RedisCacheManager();
    List<RedisCache> redisCacheList = new ArrayList<>();
    for (RedisCacheConfig redisCacheConfig : RedisCacheConfig.cacheConfigs()) {
      redisCacheList.add(new RedisCache(redisCacheConfig, redisTemplate()));
    }
    manager.setCaches(redisCacheList);
    return manager;
  }

  @Bean
  @Override
  public CacheErrorHandler errorHandler() {
    //Spring Cache默认使用的是SimpleCacheErrorHandler，会抛出所有异常，不合理，会在缓存服务有问题时导致服务失败
    return new LogTracerCacheErrorHandler();
  }

  @Bean
  public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
    final Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
        Object.class);
    final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
    return jackson2JsonRedisSerializer;
  }
}
