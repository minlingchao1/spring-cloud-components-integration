package com.minlingchao.spring.boot.dynamic.datasource.starter;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author minlingchao
 * @version 1.0
 * @date 2017/11/16 下午2:40
 * @description 数据源上下文
 */
@Slf4j
public class DynamicDataSourceContextHolder {

  /**
   * 存储已经注册的数据源的key
   */
  public static List<String> dataSourceIds = new ArrayList<>();

  /**
   * 线程级别的私有变量
   */
  private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

  public static String getDataSourceRouterKey() {
    return HOLDER.get();
  }

  public static void setDataSourceRouterKey(String dataSourceRouterKey) {
    log.info("切换至{}数据源", dataSourceRouterKey);
    HOLDER.set(dataSourceRouterKey);
  }

  /**
   * 设置数据源之前一定要先移除
   */
  public static void removeDataSourceRouterKey() {
    HOLDER.remove();
  }

  /**
   * 判断指定DataSrouce当前是否存在
   */
  public static boolean containsDataSource(String dataSourceId) {
    return dataSourceIds.contains(dataSourceId);
  }
}

