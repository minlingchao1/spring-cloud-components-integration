package com.minlingchao.spring.boot.yidun.starter;

import com.minlingchao.spring.boot.yidun.starter.config.YiDunConfig.HttpConfig;
import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: http请求封装
 * @date 2018/11/08 6:50 PM
 */
@Slf4j
public class YiDunHttpClient {

  private CloseableHttpClient client;

  public YiDunHttpClient(HttpConfig httpConfig) {
    MessageConstraints defaultMessageConstraints = MessageConstraints.custom()
        .setMaxHeaderCount(httpConfig.getMaxHeaderCount())
        .setMaxLineLength(httpConfig.getMaxLineLength())
        .build();

    ConnectionConfig defaultConnectionConfig = ConnectionConfig.custom()
        .setMalformedInputAction(CodingErrorAction.IGNORE)
        .setUnmappableInputAction(CodingErrorAction.IGNORE)
        .setCharset(Consts.UTF_8)
        .setMessageConstraints(defaultMessageConstraints)
        .build();

    RequestConfig requestConfig = RequestConfig.custom()
        .setSocketTimeout(httpConfig.getSocketTimeOut())
        .setConnectTimeout(httpConfig.getConnectTimeOut())
        .setConnectionRequestTimeout(httpConfig.getConnectionRequestTimeOut())
        .build();

    this.client = HttpClients.custom()
        .setDefaultConnectionConfig(defaultConnectionConfig)
        .setDefaultRequestConfig(requestConfig)
        .setMaxConnTotal(httpConfig.getMaxConnTotal())
        .setMaxConnPerRoute(httpConfig.getMaxConnPerRoute())
        .build();
  }

  public String execute(HttpUriRequest request) throws IOException {
    try (CloseableHttpResponse response = client.execute(request)) {
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode == HttpStatus.SC_OK) {
        return EntityUtils.toString(response.getEntity(), Consts.UTF_8);
      }

      log.error("http execute failed, uri={}, statusCode={}", request.getURI(), statusCode);
      EntityUtils.consumeQuietly(response.getEntity());
    } catch (IOException e) {
      // ignore
    }
    return null;
  }
}
