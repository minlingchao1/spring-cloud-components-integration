package com.minlingchao.spring.boot.yidun.starter.exception;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: 错误码
 * @date 2018/11/08 7:03 PM
 */
public enum YiDunErrorCode {

  OK(200, "请求成功", "OK"),
  BAD_REQUEST(400, "请求参数错误", "bad request"),
  FORBIDDEN(401, "没权限使用此接口", "forbidden"),
  PARAM_ERROR(405, "参数错误", "param error"),
  SIGNATURE_FAILURE(410, "签名验证失败", "signature failure"),
  REQUEST_EXPIRED(420, "请求时间戳不正确", "request expired"),
  REPLAY_ATTACK(430, "重放请求", "replay attack"),
  DECODE_ERROR(440, "参数解密失败", "decode error"),
  WRONG_TOKEN(450, "查询token错误", "wrong token"),
  SERVICE_UNAVAILABLE(503, "服务器内部出现异常", "service unavailable"),
  VERIFY_SIGN_ERROR(415, "签名校验错误", "verify sign error"),
  VERIFY_OK(0, "无异常", "verify ok"),
  VERIFY_PARAM_ERROR(419, "参数校验错误", "param error"),
  SYS_ERR(999, "请求错误", "sys error");


  private int code;
  private String valueCn;
  private String valueEn;


  YiDunErrorCode(int code, String valueCn, String valueEn) {
    this.code = code;
    this.valueCn = valueCn;
    this.valueEn = valueEn;
  }

  public int getCode() {
    return code;
  }

  public String getValueEn() {
    return valueEn;
  }

  public String getValueCn() {
    return valueCn;
  }
}
