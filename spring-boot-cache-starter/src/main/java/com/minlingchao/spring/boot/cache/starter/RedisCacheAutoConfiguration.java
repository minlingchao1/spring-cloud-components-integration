package com.minlingchao.spring.boot.cache.starter;

import com.minlingchao.spring.boot.cache.starter.annotation.EnableRedisCaching;
import com.minlingchao.spring.boot.cache.starter.config.CacheRedisConfig;
import com.minlingchao.spring.boot.cache.starter.config.RedisCachingConfigurerSupport;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: redis缓存自动配置
 * @date 2018/11/27 2:46 PM
 */
@Configuration
@EnableRedisCaching
@ConditionalOnClass({LettuceConnection.class, RedisOperations.class, Lettuce.class})
public class RedisCacheAutoConfiguration extends RedisCachingConfigurerSupport {

  @Bean
  @Primary
  @Qualifier("cacheProperties")
  public RedisProperties redisProperties() {
    return new CacheRedisConfig();
  }

  @Bean
  @Qualifier("cacheFactory")
  public LettuceConnectionFactory lettuceConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setDatabase(redisProperties().getDatabase());
    redisStandaloneConfiguration.setHostName(redisProperties().getHost());
    redisStandaloneConfiguration.setPort(redisProperties().getPort());
    redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties().getPassword()));

    LettuceClientConfiguration.LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder = LettuceClientConfiguration
        .builder();
    LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration,
        lettuceClientConfigurationBuilder.build());
    return factory;
  }


  @Override
  @Bean
  @Qualifier("cacheRedis")
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    Jackson2JsonRedisSerializer<Object> serializer = jackson2JsonRedisSerializer();
    redisTemplate.setConnectionFactory(lettuceConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(serializer);
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

    return redisTemplate;
  }
}
