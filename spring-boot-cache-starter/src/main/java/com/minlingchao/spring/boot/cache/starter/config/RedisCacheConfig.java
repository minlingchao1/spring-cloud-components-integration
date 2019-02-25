package com.minlingchao.spring.boot.cache.starter.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 提供了几个常用的缓存配置（缓存名称+缓存失效时间 一一对应） Spring Cache的注解不支持配置过期时间，我们通过缓存的名称来选择对应的过期时间
 */
public class RedisCacheConfig {

  public static final String MINUTE_1 = "MINUTE_1";
  public static final String MINUTE_10 = "MINUTE_10";
  public static final String MINUTE_30 = "MINUTE_30";
  public static final String HOUR_1 = "HOUR_1";
  public static final String HOUR_4 = "HOUR_4";
  public static final String HOUR_12 = "HOUR_12";
  public static final String DAY_1 = "DAY_1";
  public static final String DAY_7 = "DAY_7";
  public static final String DAY_30 = "DAY_30";
  public static final String DAY_365 = "DAY_365";
  public static final String FOREVER = "FOREVER";

  public static final String MINUTE_1_CACHE_NULL = "MINUTE_1_CACHE_NULL";
  public static final String MINUTE_10_CACHE_NULL = "MINUTE_10_CACHE_NULL";
  public static final String MINUTE_30_CACHE_NULL = "MINUTE_30_CACHE_NULL";
  public static final String HOUR_1_CACHE_NULL = "HOUR_1_CACHE_NULL";
  public static final String HOUR_4_CACHE_NULL = "HOUR_4_CACHE_NULL";
  public static final String HOUR_12_CACHE_NULL = "HOUR_12_CACHE_NULL";
  public static final String DAY_1_CACHE_NULL = "DAY_1_CACHE_NULL";
  public static final String DAY_3_CACHE_NULL = "DAY_3_CACHE_NULL";
  public static final String DAY_7_CACHE_NULL = "DAY_7_CACHE_NULL";
  public static final String DAY_30_CACHE_NULL = "DAY_30_CACHE_NULL";
  public static final String DAY_365_CACHE_NULL = "DAY_365_CACHE_NULL";
  public static final String FOREVER_CACHE_NULL = "FOREVER_CACHE_NULL";

  public static List<RedisCacheConfig> cacheConfigs() {
    List<RedisCacheConfig> RedisCacheConfigs = new ArrayList<>();

    RedisCacheConfigs.add(new RedisCacheConfig(MINUTE_1, 1, TimeUnit.MINUTES, false));
    RedisCacheConfigs.add(new RedisCacheConfig(MINUTE_10, 10, TimeUnit.MINUTES, false));
    RedisCacheConfigs.add(new RedisCacheConfig(MINUTE_30, 30, TimeUnit.MINUTES, false));
    RedisCacheConfigs.add(new RedisCacheConfig(HOUR_1, 1, TimeUnit.HOURS, false));
    RedisCacheConfigs.add(new RedisCacheConfig(HOUR_4, 4, TimeUnit.HOURS, false));
    RedisCacheConfigs.add(new RedisCacheConfig(HOUR_12, 12, TimeUnit.HOURS, false));
    RedisCacheConfigs.add(new RedisCacheConfig(DAY_1, 1, TimeUnit.DAYS, false));
    RedisCacheConfigs.add(new RedisCacheConfig(DAY_7, 7, TimeUnit.DAYS, false));
    RedisCacheConfigs.add(new RedisCacheConfig(DAY_30, 30, TimeUnit.DAYS, false));
    RedisCacheConfigs.add(new RedisCacheConfig(DAY_365, 365, TimeUnit.DAYS, false));
    RedisCacheConfigs.add(new RedisCacheConfig(FOREVER, -1, null, false));

    RedisCacheConfigs.add(new RedisCacheConfig(MINUTE_1_CACHE_NULL, 1, TimeUnit.MINUTES, true));
    RedisCacheConfigs.add(new RedisCacheConfig(MINUTE_10_CACHE_NULL, 10, TimeUnit.MINUTES, true));
    RedisCacheConfigs.add(new RedisCacheConfig(MINUTE_30_CACHE_NULL, 30, TimeUnit.MINUTES, true));
    RedisCacheConfigs.add(new RedisCacheConfig(HOUR_1_CACHE_NULL, 1, TimeUnit.HOURS, true));
    RedisCacheConfigs.add(new RedisCacheConfig(HOUR_4_CACHE_NULL, 4, TimeUnit.HOURS, true));
    RedisCacheConfigs.add(new RedisCacheConfig(HOUR_12_CACHE_NULL, 12, TimeUnit.HOURS, true));
    RedisCacheConfigs.add(new RedisCacheConfig(DAY_1_CACHE_NULL, 1, TimeUnit.DAYS, true));
    RedisCacheConfigs.add(new RedisCacheConfig(DAY_3_CACHE_NULL, 3, TimeUnit.DAYS, true));
    RedisCacheConfigs.add(new RedisCacheConfig(DAY_7_CACHE_NULL, 7, TimeUnit.DAYS, true));
    RedisCacheConfigs.add(new RedisCacheConfig(DAY_30_CACHE_NULL, 30, TimeUnit.DAYS, true));
    RedisCacheConfigs.add(new RedisCacheConfig(DAY_365_CACHE_NULL, 365, TimeUnit.DAYS, true));
    RedisCacheConfigs.add(new RedisCacheConfig(FOREVER_CACHE_NULL, -1, null, true));
    return RedisCacheConfigs;
  }

  private String name;
  private long expire;
  private TimeUnit timeUnit;
  private int syncRetry = 5;
  private long syncExpire = 5000;
  private long syncSleep = 100;
  private boolean cacheNull = false;
  private boolean enhanceEnable = true;

  public RedisCacheConfig(String name, long expire, TimeUnit timeUnit, boolean cacheNull) {
    this.name = name;
    this.expire = expire;
    this.timeUnit = timeUnit;
    this.cacheNull = cacheNull;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getExpire() {
    return expire;
  }

  public void setExpire(long expire) {
    this.expire = expire;
  }

  public TimeUnit getTimeUnit() {
    return timeUnit;
  }

  public void setTimeUnit(TimeUnit timeUnit) {
    this.timeUnit = timeUnit;
  }

  public int getSyncRetry() {
    return syncRetry;
  }

  public void setSyncRetry(int syncRetry) {
    this.syncRetry = syncRetry;
  }

  public long getSyncExpire() {
    return syncExpire;
  }

  public void setSyncExpire(long syncExpire) {
    this.syncExpire = syncExpire;
  }

  public long getSyncSleep() {
    return syncSleep;
  }

  public void setSyncSleep(long syncSleep) {
    this.syncSleep = syncSleep;
  }

  public boolean isCacheNull() {
    return cacheNull;
  }

  public void setCacheNull(boolean cacheNull) {
    this.cacheNull = cacheNull;
  }

  public boolean isEnhanceEnable() {
    return enhanceEnable;
  }

  public void setEnhanceEnable(boolean enhanceEnable) {
    this.enhanceEnable = enhanceEnable;
  }
}


