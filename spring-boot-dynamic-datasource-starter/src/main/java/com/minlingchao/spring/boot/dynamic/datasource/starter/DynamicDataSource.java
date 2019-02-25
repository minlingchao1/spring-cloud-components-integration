package com.minlingchao.spring.boot.dynamic.datasource.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author minlingchao
 * @version 1.0
 * @date 2017/11/16 下午2:39
 * @description 多数据源配置
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

  @Override
  protected Object determineCurrentLookupKey() {
    String dataSourceName = DynamicDataSourceContextHolder.getDataSourceRouterKey();
    log.info("当前数据源是：{}", dataSourceName);
    return DynamicDataSourceContextHolder.getDataSourceRouterKey();
  }
}
