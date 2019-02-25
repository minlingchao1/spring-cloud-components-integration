package com.minlingchao.spring.boot.yidun.starter;

import static com.minlingchao.spring.boot.yidun.starter.exception.YiDunErrorCode.BAD_REQUEST;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.minlingchao.spring.boot.common.util.DateUtil;
import com.minlingchao.spring.boot.common.util.GsonUtil;
import com.minlingchao.spring.boot.common.util.StringUtil;
import com.minlingchao.spring.boot.yidun.starter.config.YiDunConfig.LogConfig;
import com.minlingchao.spring.boot.yidun.starter.config.YiDunConfig.YiDunVerifyConfig;
import com.minlingchao.spring.boot.yidun.starter.dto.YiDunVerifyRequestDTO;
import com.minlingchao.spring.boot.yidun.starter.dto.YiDunVerifyResponseDTO;
import com.minlingchao.spring.boot.yidun.starter.exception.YiDunErrorCode;
import com.minlingchao.spring.boot.yidun.starter.exception.YiDunException;
import com.minlingchao.spring.boot.yidun.starter.util.YiDunSignUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author minlingchao
 * @version V1.0
 * @Description:
 * @date 2018/11/09 1:35 PM
 */
@Slf4j
public class YiDunVerifyTemplate {

  private YiDunVerifyConfig verifyConfig;

  private LogConfig logConfig;

  private YiDunHttpClient client;

  private static final String VERIFY_URL = "/api/v2/verify";

  private static final String CAPTCHA_ID = "captchaId";

  private static final String VALIDATE = "validate";

  private static final String USER = "user";

  private static final String SECRET_ID = "secretId";

  private static final String VERSION = "version";

  private static final String TIMESTMAP = "timestamp";

  private static final String NONCE = "nonce";

  private static final String SIGNATURE = "signature";

  public YiDunVerifyTemplate(
      YiDunVerifyConfig verifyConfig, LogConfig logConfig, YiDunHttpClient client) {
    this.verifyConfig = verifyConfig;
    this.client = client;
    this.logConfig = logConfig;
  }

  /**
   * 服务端校验
   */
  public YiDunVerifyResponseDTO verify(YiDunVerifyRequestDTO verifyRequestDTO)
      throws UnsupportedEncodingException {

    if (StringUtils.isBlank(verifyRequestDTO.getValidate())) {
      throw new YiDunException(BAD_REQUEST);
    }
    RequestBuilder requestBuilder = RequestBuilder.post(verifyConfig.getHost() + VERIFY_URL);
    Map<String, String> params = getParams(verifyRequestDTO);

    List<NameValuePair> formParams = Lists.newArrayList();
    for (Map.Entry<String, String> param : params.entrySet()) {
      formParams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
    }
    Charset encoding = Charset.forName("utf8");
    UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(formParams, encoding);

    HttpUriRequest request = requestBuilder.setEntity(postEntity).build();

    if (logConfig.getEnable()) {
      log.info("[YIDUN VERIFY REQ]:path:{},header:{},params:{}", request.getURI().toString(),
          request.getAllHeaders(), params.toString());
    }

    try {
      String result = client.execute(request);
      if (logConfig.getEnable()) {
        log.info("YIDUN VERIFY REQ RESP:{}", result);
      }

      if (StringUtils.isBlank(result)) {
        throw new YiDunException(YiDunErrorCode.SYS_ERR);
      }
      YiDunVerifyResponseDTO responseDTO = GsonUtil.fromJson(result, YiDunVerifyResponseDTO.class);

      if (responseDTO.getError() != 0) {
        throw new YiDunException(responseDTO.getError(), responseDTO.getMsg());
      }
      return responseDTO;
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new YiDunException(YiDunErrorCode.SYS_ERR);
    }
  }

  private Map<String, String> getParams(YiDunVerifyRequestDTO verifyRequestDTO)
      throws UnsupportedEncodingException {
    String nonce = StringUtil.randomNumeric(6);
    String curTime = String.valueOf(DateUtil.currentMills());
    Map<String, String> params = Maps.newHashMap();
    params.put(CAPTCHA_ID, verifyConfig.getCaptchaId());
    params.put(USER,
        StringUtils.isBlank(verifyRequestDTO.getUser()) ? "" : verifyRequestDTO.getUser());
    params.put(VALIDATE, verifyRequestDTO.getValidate());
    params.put(SECRET_ID, verifyConfig.getSecretId());
    params.put(VERSION, "v2");
    params.put(TIMESTMAP, curTime);
    params.put(NONCE, nonce);

    String checkSum = YiDunSignUtil.genSignature(verifyConfig.getSecretKey(), params);

    params.put(SIGNATURE, checkSum);
    return params;
  }
}
