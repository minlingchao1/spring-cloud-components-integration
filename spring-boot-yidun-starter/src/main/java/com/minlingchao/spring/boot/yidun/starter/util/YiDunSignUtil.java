package com.minlingchao.spring.boot.yidun.starter.util;

import com.minlingchao.spring.boot.yidun.starter.exception.YiDunErrorCode;
import com.minlingchao.spring.boot.yidun.starter.exception.YiDunException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: 签名工具
 * @date 2018/11/08 7:00 PM
 */
public class YiDunSignUtil {

  public static String genSignature(String secretKey, Map<String, String> params)
      throws UnsupportedEncodingException {
    if (secretKey == null || params == null || params.size() == 0) {
      throw new YiDunException(YiDunErrorCode.PARAM_ERROR);
    }

    // 1. 参数名按照ASCII码表升序排序
    String[] keys = params.keySet().toArray(new String[0]);

    Arrays.sort(keys);

    // 2. 按照排序拼接参数名与参数值
    StringBuilder sb = new StringBuilder();
    for (String key : keys) {
      sb.append(key).append(params.get(key));
    }

    // 3. 将secretKey拼接到最后
    sb.append(secretKey);

    // 4. MD5是128位长度的摘要算法，用16进制表示，一个十六进制的字符能表示4个位，所以签名后的字符串长度固定为32个十六进制字符。
    return DigestUtils.md5Hex(sb.toString().getBytes("UTF-8"));
  }
}
