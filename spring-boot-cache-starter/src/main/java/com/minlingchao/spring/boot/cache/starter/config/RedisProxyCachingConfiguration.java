package com.minlingchao.spring.boot.cache.starter.config;

import com.minlingchao.spring.boot.cache.starter.annotation.EnableRedisCaching;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cache.annotation.ProxyCachingConfiguration;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @see ProxyCachingConfiguration
 */
@Configuration
public class RedisProxyCachingConfiguration extends ProxyCachingConfiguration {

  @Override
  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public CacheInterceptor cacheInterceptor() {
    CacheInterceptor interceptor = new RedisCacheInterceptor();
    interceptor.setCacheOperationSources(cacheOperationSource());
    if (this.cacheResolver != null) {
      interceptor.setCacheResolver(this.cacheResolver);
    } else if (this.cacheManager != null) {
      interceptor.setCacheManager(this.cacheManager);
    }
    if (this.keyGenerator != null) {
      interceptor.setKeyGenerator(this.keyGenerator);
    }
    if (this.errorHandler != null) {
      interceptor.setErrorHandler(this.errorHandler);
    }
    return interceptor;
  }

  @Override
  public void setImportMetadata(AnnotationMetadata importMetadata) {
    this.enableCaching = AnnotationAttributes.fromMap(
        importMetadata.getAnnotationAttributes(EnableRedisCaching.class.getName(), false));
    if (this.enableCaching == null) {
      throw new IllegalArgumentException(
          "@EnableRedisCaching is not present on importing class " + importMetadata.getClassName());
    }
  }
}
