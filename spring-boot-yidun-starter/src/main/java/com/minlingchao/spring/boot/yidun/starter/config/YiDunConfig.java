package com.minlingchao.spring.boot.yidun.starter.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: 易盾工具类
 * @date 2018/11/08 6:45 PM
 */
@Data
@ConfigurationProperties(prefix = "integration.yidun")
public class YiDunConfig {

  /**
   * 行为验证码配置
   */
  private YiDunVerifyConfig verify;

  /**
   * http请求配置
   */
  private HttpConfig http = HttpConfig.builder().build();

  /**
   * 日志配置
   */
  private LogConfig log = LogConfig.builder().build();

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class YiDunVerifyConfig {

    /**
     * 请求地址
     */
    private String host;

    /**
     * 易盾为产品分配的密钥id
     */
    private String secretId;

    /**
     * 与secretId一一对应的密钥，请妥善保管此密钥，是双方通信的签名凭证
     */
    private String secretKey;

    /**
     * 验证码id
     */
    private String captchaId;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class HttpConfig {

    /**
     * socket超时时间
     */
    @Builder.Default
    private Integer socketTimeOut = 3000;

    /**
     * 链接超时
     */
    @Builder.Default
    private Integer connectTimeOut = 3000;

    /**
     * 请求超时
     */
    @Builder.Default
    private Integer connectionRequestTimeOut = 3000;

    /**
     * 最大header数
     */
    @Builder.Default
    private Integer maxHeaderCount = 200;

    /**
     * 最大长度
     */
    @Builder.Default
    private Integer maxLineLength = 2000;

    /**
     * 最大连接数
     */
    @Builder.Default
    private Integer maxConnTotal = 200;

    /**
     * 分配给同一个route(路由)最大的并发连接数。
     */
    @Builder.Default
    private Integer maxConnPerRoute = 50;
  }


  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class LogConfig {

    @Builder.Default
    private Boolean enable = true;
  }
}
