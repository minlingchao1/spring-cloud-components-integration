package com.minlingchao.spring.boot.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * @author minlingchao
 * @version V1.0
 * @Description:
 * @date 2018/11/27 5:30 PM
 */
public class HttpUtil {

  /**
   * 匹配域名
   */
  private static final Pattern DOMAIN_PATTERN = Pattern.compile("(?:[0-9a-z_!~*'()-]+\\.)*"
      + "(?:[0-9a-z][0-9a-z!~*#&'.^:@+$%-]{0,61})?[0-9a-z]\\."
      + "[a-z]{0,6}"
      + "(?::[0-9]{1,4})?", Pattern.CASE_INSENSITIVE);


  public static String getDomain(String url) {
    String domain = StringUtils.EMPTY;
    Matcher m = DOMAIN_PATTERN.matcher(url);
    if (m.find()) {
      domain = m.group();
    }
    return domain;
  }
}
