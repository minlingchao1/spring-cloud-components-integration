package com.minlingchao.spring.boot.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Title: IPRange
 * @date 2018-12-20 15:38
 **/
@Data
public class IPRange {

  private long beginIp = 0;
  private long endIp = 0;

  /**
   * constructor <code>ipRange</code>
   *
   * @param ipRange ip range eg: 192.168.1.*-192.168.2.*;192.168.1.*
   */
  public IPRange(String ipRange) {
    try {
      if (StringUtils.isBlank(ipRange)) {
        return;
      }

      ipRange = StringUtils.trim(ipRange);

      String[] ips = StringUtils.split(ipRange, "-");
      beginIp = addresstoInt(StringUtils.replace(ips[0], "*", "0"));

      if (ips.length == 1) {
        endIp = addresstoInt(StringUtils.replace(ips[0], "*", "255"));
      } else {
        endIp = addresstoInt(StringUtils.replace(ips[1], "*", "255"));
      }
    } catch (Exception e) {
    }
  }

  private long addresstoInt(String ip) {
    byte[] bytes;
    try {
      bytes = InetAddress.getByName(ip).getAddress();
    } catch (UnknownHostException e) {
      e.printStackTrace();
      return 0;
    }
    long resp = ((bytes[0] << 24) & 0xff000000) | ((bytes[1] << 16) & 0x00ff0000) | ((bytes[2] << 8)
        & 0x0000ff00) | (bytes[3] & 0x000000ff);

    if (resp < 0) {
      resp += 0x100000000L;
    }

    return resp;
  }

  /**
   * check ip range is valid
   */
  public boolean isValid() {
    if ((beginIp > 0) && (endIp > 0)) {
      return true;
    }

    return false;
  }

  /**
   * check ip in range
   */
  public boolean isInRange(String ip) {
    boolean result = true;
    long intIp = addresstoInt(ip);

    if ((intIp > endIp) || (intIp < beginIp)) {
      result = false;
    }

    return result;
  }

}
