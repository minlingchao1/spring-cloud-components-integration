package com.minlingchao.spring.boot.cache.starter.config;

import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by hzcaojiajun on 2018/6/6.
 */
public class RedisCacheThreadLocal {

  public static ThreadLocal<MethodInvocation> invocationThreadLocal = new ThreadLocal<>();
}
