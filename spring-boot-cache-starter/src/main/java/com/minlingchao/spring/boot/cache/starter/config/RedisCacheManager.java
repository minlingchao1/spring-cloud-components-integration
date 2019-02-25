package com.minlingchao.spring.boot.cache.starter.config;

import java.util.Collection;
import org.springframework.cache.support.AbstractCacheManager;

/**
 * spring cache自带的RedisCacheManager不能使用NCR（NCR不支持Transaction)，因此用接口自己封装一个
 */
public class RedisCacheManager extends AbstractCacheManager {

  private Collection<? extends RedisCache> caches;
  private boolean usePrefix = false;

  public void setCaches(Collection<? extends RedisCache> caches) {
    this.caches = caches;
    this.updateUsePrefixConfig();
  }

  public void setUsePrefix(boolean usePrefix) {
    this.usePrefix = usePrefix;
    this.updateUsePrefixConfig();
  }

  private void updateUsePrefixConfig() {
    if (this.caches != null) {
      for (RedisCache redisCache : this.caches) {
        redisCache.setUsePrefix(usePrefix);
      }
    }
  }

  @Override
  protected Collection<? extends RedisCache> loadCaches() {
    return this.caches;
  }
}
