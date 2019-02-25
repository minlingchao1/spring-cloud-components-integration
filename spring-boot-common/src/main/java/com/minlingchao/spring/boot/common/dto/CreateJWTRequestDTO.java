package com.minlingchao.spring.boot.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: 创建jwt token dto
 * @date 2018/10/22 11:37 AM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateJWTRequestDTO<T> {

  /**
   * token 详情
   */
  private T jwtInfo;
  

  /**
   * 有效时间
   */
  private Long expireInMills;

  /**
   * base64加密
   */
  private String secret;

}
