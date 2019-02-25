package com.minlingchao.spring.boot.common.dao;

import java.util.List;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: Dao基础接口
 * @date 2018/10/16 4:06 PM
 */
public interface Dao<T> {

  /**
   * 获取全部记录数量
   */
  int countAll();

  /**
   * 根据id获取记录
   */
  T getById(Long id);

  /**
   * 获取全部记录列表
   */
  List<T> getAll();

  /**
   * 添加
   */
  int insert(T t);

  /**
   * 更新记录
   */
  int updateById(T t);

  /**
   * 批量更新
   */
  int updateBatch(List<T> list);

  /**
   * 根据id删除
   */
  int deleteById(T t);
}
