package com.minlingchao.spring.boot.cache.starter.annotation;

import com.minlingchao.spring.boot.cache.starter.config.RedisCachingConfigurationSelector;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisCachingConfigurationSelector.class)
public @interface EnableRedisCaching {

  boolean proxyTargetClass() default false;

  AdviceMode mode() default AdviceMode.PROXY;

  int order() default Ordered.LOWEST_PRECEDENCE;
}
