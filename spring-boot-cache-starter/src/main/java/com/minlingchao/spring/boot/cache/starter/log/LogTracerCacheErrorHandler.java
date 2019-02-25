package com.minlingchao.spring.boot.cache.starter.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

public class LogTracerCacheErrorHandler implements CacheErrorHandler {

  private static final Logger logger = LoggerFactory.getLogger(LogTracerCacheErrorHandler.class);

  @Override
  public void handleCacheGetError(RuntimeException e, Cache cache, Object o) {
    logger.error("cache get error, cache = {}, key = {}", cache.getName(), o, e);
  }

  @Override
  public void handleCachePutError(RuntimeException e, Cache cache, Object o, Object o1) {
    logger.error("cache put error, cache = {}, key = {}", cache.getName(), o, e);
  }

  @Override
  public void handleCacheEvictError(RuntimeException e, Cache cache, Object o) {
    logger.error("cache evict error, cache = {}, key = {}", cache.getName(), o, e);
  }

  @Override
  public void handleCacheClearError(RuntimeException e, Cache cache) {
    logger.error("cache clear error, cache = {}", cache.getName(), e);
  }
}
