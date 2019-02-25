package com.minlingchao.spring.boot.yidun.starter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: 行为验证码请求
 * @date 2018/11/09 1:38 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YiDunVerifyRequestDTO {

  /**
   * validate
   */
  private String validate;

  /**
   * 用户信息，值可为空
   */
  private String user;
}
