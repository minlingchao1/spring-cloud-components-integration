package com.minlingchao.spring.boot.cache.starter.config;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.interceptor.CacheInterceptor;

/**
 * @see CacheInterceptor
 */
public class RedisCacheInterceptor extends CacheInterceptor {

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    RedisCacheThreadLocal.invocationThreadLocal.set(invocation);
    return super.invoke(invocation);
  }

}
