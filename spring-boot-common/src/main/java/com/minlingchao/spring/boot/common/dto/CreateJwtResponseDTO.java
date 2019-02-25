package com.minlingchao.spring.boot.common.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: jwt token生成返回
 * @date 2018/10/22 1:35 PM
 */
@Data
@Builder
public class CreateJwtResponseDTO {

  /**
   * 生成token
   */
  private String token;

  /**
   * 超时时间点
   */
  private Long expireAt;
}
