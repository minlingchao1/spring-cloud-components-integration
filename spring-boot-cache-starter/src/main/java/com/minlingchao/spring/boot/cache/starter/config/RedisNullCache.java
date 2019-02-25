package com.minlingchao.spring.boot.cache.starter.config;

class RedisNullCache {

  private int id = 0;
  final static RedisNullCache INSTANCE = new RedisNullCache();

  private RedisNullCache() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
