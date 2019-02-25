package com.minlingchao.spring.boot.yidun.starter.dto;

import lombok.Data;

/**
 * @author minlingchao
 * @version V1.0
 * @Description:
 * @date 2018/11/09 1:46 PM
 */
@Data
public class YiDunVerifyResponseDTO {

  /**
   * 二次校验结果 true:校验通过 false:校验失败
   */
  private Boolean result;

  /**
   * 异常代号
   */
  private Integer error;

  /**
   * 错误描述信息
   */
  private String msg;
}
