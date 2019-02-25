package com.minlingchao.spring.boot.yidun.starter.exception;

import lombok.Getter;

/**
 * @author minlingchao
 * @version V1.0
 * @Description:
 * @date 2018/11/08 6:58 PM
 */
public class YiDunException extends RuntimeException {

  /**
   * 错误码
   */
  @Getter
  private Integer errorCode;

  /**
   * 错误信息
   */
  @Getter
  private String errorMessage;

  public YiDunException(Integer errorCode, String errorMessage) {
    super(errorMessage);
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public YiDunException(YiDunErrorCode errorCode) {
    this(errorCode.getCode(), errorCode.getValueCn());
  }
}
