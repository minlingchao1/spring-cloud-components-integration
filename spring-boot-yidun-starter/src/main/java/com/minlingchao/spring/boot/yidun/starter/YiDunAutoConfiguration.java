package com.minlingchao.spring.boot.yidun.starter;

import com.minlingchao.spring.boot.yidun.starter.config.YiDunConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author minlingchao
 * @version V1.0
 * @Description:
 * @date 2018/11/08 6:42 PM
 */
@Configuration
@EnableConfigurationProperties(YiDunConfig.class)
@Slf4j
public class YiDunAutoConfiguration {


  @Bean
  public YiDunHttpClient yiDunHttpClient(YiDunConfig yiDunConfig) {
    return new YiDunHttpClient(yiDunConfig.getHttp());
  }

  @Bean
  public YiDunVerifyTemplate yiDunVerifyTemplate(YiDunConfig yiDunConfig,
      YiDunHttpClient yiDunHttpClient) {
    return new YiDunVerifyTemplate(yiDunConfig.getVerify(), yiDunConfig.getLog(),
        yiDunHttpClient);
  }
}
