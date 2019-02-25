package com.minlingchao.spring.boot.cache.starter.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author minlingchao
 * @version V1.0
 * @Description:
 * @date 2018/11/27 3:20 PM
 */
@ConfigurationProperties(prefix = "integration.cache.redis")
public class CacheRedisConfig extends RedisProperties {

}
