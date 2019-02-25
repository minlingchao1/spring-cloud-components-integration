package com.minlingchao.spring.boot.cache.starter.config;

import com.minlingchao.spring.boot.cache.starter.annotation.EnableRedisCaching;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.util.ClassUtils;

/**
 * @author minlingchao
 * @version V1.0
 * @Description:
 * @date 2018/11/27 2:47 PM
 */
public class RedisCachingConfigurationSelector extends
    AdviceModeImportSelector<EnableRedisCaching> {

  private static final String PROXY_JCACHE_CONFIGURATION_CLASS =
      "org.springframework.cache.jcache.config.ProxyJCacheConfiguration";

  private static final String CACHE_ASPECT_CONFIGURATION_CLASS_NAME =
      "org.springframework.cache.aspectj.AspectJCachingConfiguration";

  private static final String JCACHE_ASPECT_CONFIGURATION_CLASS_NAME =
      "org.springframework.cache.aspectj.AspectJJCacheConfiguration";


  private static final boolean jsr107Present = ClassUtils.isPresent(
      "javax.cache.Cache", RedisCachingConfigurationSelector.class.getClassLoader());

  private static final boolean jcacheImplPresent = ClassUtils.isPresent(
      PROXY_JCACHE_CONFIGURATION_CLASS, RedisCachingConfigurationSelector.class.getClassLoader());


  @Override
  protected String[] selectImports(AdviceMode adviceMode) {
    switch (adviceMode) {
      case PROXY:
        return getProxyImports();
      case ASPECTJ:
        return getAspectJImports();
      default:
        return null;
    }
  }

  private String[] getProxyImports() {
    List<String> result = new ArrayList<String>();
    result.add(AutoProxyRegistrar.class.getName());
    result.add(RedisProxyCachingConfiguration.class.getName());
    if (jsr107Present && jcacheImplPresent) {
      result.add(PROXY_JCACHE_CONFIGURATION_CLASS);
    }
    return result.toArray(new String[result.size()]);
  }

  private String[] getAspectJImports() {
    List<String> result = new ArrayList<String>();
    result.add(CACHE_ASPECT_CONFIGURATION_CLASS_NAME);
    if (jsr107Present && jcacheImplPresent) {
      result.add(JCACHE_ASPECT_CONFIGURATION_CLASS_NAME);
    }
    return result.toArray(new String[result.size()]);
  }
}
