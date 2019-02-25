package com.minlingchao.spring.boot.common.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: 基础model类
 * @date 2018/10/16 4:11 PM
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class BaseModel implements Serializable {

  /**
   * 数据库id
   */
  @Expose
  private Long id;

  /**
   * 创建时间戳
   */
  @Expose
  private Date createTime;

  /**
   * 更新时间戳
   */
  @Expose
  private Date updateTime;
}
