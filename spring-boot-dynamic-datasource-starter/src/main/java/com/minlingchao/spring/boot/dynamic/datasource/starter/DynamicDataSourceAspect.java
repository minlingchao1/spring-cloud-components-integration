package com.minlingchao.spring.boot.dynamic.datasource.starter;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.config.Order;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author minlingchao
 * @version 1.0
 * @date 2017/11/16 下午2:55
 * @description
 */
@Aspect
@Order(-10)// 保证该AOP在@Transactional之前执行
@Component
@Slf4j
public class DynamicDataSourceAspect {

  @Before("@annotation(ds)")
  public void changeDataSource(JoinPoint point, TargetDataSource ds) throws Throwable {
    String dsId = ds.name();
    if (!DynamicDataSourceContextHolder.containsDataSource(dsId)) {
      log.error("数据源[{}]不存在，使用默认数据源 > {}", ds.name(), point.getSignature());
    } else {
      log.debug("Use DataSource : {} > {}", ds.name(), point.getSignature());
      DynamicDataSourceContextHolder.setDataSourceRouterKey(ds.name());
    }
  }

  @After("@annotation(ds)")
  public void restoreDataSource(JoinPoint point, TargetDataSource ds) {
    log.debug("Revert DataSource : {} > {}", ds.name(), point.getSignature());
    DynamicDataSourceContextHolder.removeDataSourceRouterKey();
  }
}
