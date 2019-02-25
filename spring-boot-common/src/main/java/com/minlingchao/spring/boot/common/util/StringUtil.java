package com.minlingchao.spring.boot.common.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nonnull;

/**
 * 针对字符串的工具类
 *
 * @author niele
 * @date 2018/10/22 19:19
 */
public class StringUtil {

  private static final String RANDOM_CHAR_ALL = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String RANDOM_CHAR_ALPHABETIC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String RANDOM_CHAR_NUMERIC = "0123456789";

  @Deprecated
  public static String getRandomString(int length) {
    StringBuilder builder = new StringBuilder();
    Random random = ThreadLocalRandom.current();
    for (int i = 0; i < length; i++) {
      builder.append(RANDOM_CHAR_ALL.charAt(random.nextInt(RANDOM_CHAR_ALL.length())));
    }
    return builder.toString();
  }

  @Deprecated
  public static String getBase64RandomString(int length) {
    return getRandomString(length);
  }

  /**
   * 根据 chars 提供的随机生成指定长度的字符串
   *
   * @param length 随机字符串长度
   * @param chars 随机字符串的值
   * @return 随机字符串
   */
  public static String random(int length, @Nonnull String chars) {
    StringBuilder builder = new StringBuilder();
    Random random = ThreadLocalRandom.current();
    for (int i = 0; i < length; i++) {
      builder.append(chars.charAt(random.nextInt(chars.length())));
    }

    return builder.toString();
  }

  /**
   * 根据设置的字符区间获取随机字符串
   *
   * @param length 随机字符的长度
   * @param start 随机字符范围（开始）
   * @param end 随机字符的范围（结束）
   * @return 随时字符串
   */
  public static String random(int length, int start, int end) {
    Random random = new Random();
    StringBuilder buffer = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int randomLimitedInt = start + (int) (random.nextFloat() * (end - start + 1));
      buffer.append((char) randomLimitedInt);
    }

    return buffer.toString();
  }

  /**
   * Characters will be chosen from the set of Latin alphabetic characters (a-z, A-Z).
   */
  public static String randomAlphabetic(int length) {
    return random(length, RANDOM_CHAR_ALPHABETIC);
  }

  /**
   * Characters will be chosen from the set of \p{Digit} characters.
   */
  public static String randomNumeric(int length) {
    return random(length, RANDOM_CHAR_NUMERIC);
  }


  /**
   * 默认随机串，包含英文大小写以及数字
   *
   * @param length 制定字符串的长度
   * @return 随机字符串
   */
  public static String random(int length) {
    return random(length, RANDOM_CHAR_ALL);
  }


  /**
   * 随时生成可显示 Ascii 字符的串
   *
   * @param length 随时字符串的长度
   * @return 随机字符串
   */
  public static String randomAscii(int length) {
    return random(length, 33, 126);
  }


  /**
   * 判断字符是否相等
   */
  public static boolean equal(@Nonnull String a, @Nonnull String b) {
    return a.equals(b);
  }
}
