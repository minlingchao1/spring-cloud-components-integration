package com.minlingchao.spring.boot.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.annotation.Nonnull;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: md5工具类
 * @date 2018/10/19 9:06 PM
 */
public class MD5Util {


  /**
   * 使用系统的 MD5 包 Hash 字符串
   *
   * @param input 需要 Hash 的字符串
   * @return Hahsh 以后的字符串
   * @throws NoSuchAlgorithmException 如果系统不支持，在直接抛出异常
   */
  public static String hash(@Nonnull String input) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("MD5");
    digest.update(input.getBytes());
    return DatatypeConverter.printHexBinary(digest.digest()).toLowerCase();
  }


  /**
   * 使用系统的 MD5 包 Hash 字符串，并返回大写的串
   *
   * @param input 需要 Hash 的字符串
   * @return Hahsh 以后的字符串
   * @throws NoSuchAlgorithmException 如果系统不支持，在直接抛出异常
   */
  public static String hashWithUpperCase(@Nonnull String input) throws NoSuchAlgorithmException {
    return hash(input).toUpperCase();
  }


  /**
   * 使用系统的 MD5 包 Hash 字符串，并加盐
   *
   * @param input 需要 Hash 的字符串
   * @return Hahsh 以后的字符串
   * @throws NoSuchAlgorithmException 如果系统不支持，在直接抛出异常
   */
  public static String hashWithSalt(@Nonnull String input, @Nonnull String salt)
      throws NoSuchAlgorithmException {
    return hash(input + salt);
  }

  /**
   * md5文件
   * @param file
   * @return
   */
  public static String hashFile(@Nonnull File file)
      throws IOException, NoSuchAlgorithmException {
    InputStream inputStream=new FileInputStream(file);
    byte[] buffer = new byte[8192];
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    int len;
    while((len = inputStream.read(buffer)) != -1){
      md5.update(buffer, 0, len);
    }
    inputStream.close();
    return DatatypeConverter.printHexBinary(md5.digest()).toLowerCase();
  }

  /**
   * 文件md5大写
   * @param file
   * @return
   * @throws IOException
   * @throws NoSuchAlgorithmException
   */
  public static String hashFileWithUpperCase(@Nonnull File file)
      throws IOException, NoSuchAlgorithmException {
    return hashFile(file).toUpperCase();
  }


  /**
   * 加盐加密
   */
  @Deprecated
  public static final String MD5WithSalt(String s, String salt) {
    String str = s + salt;
    return MD5(str);
  }

  @Deprecated
  public final static String MD5(String s) {
    char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
        'f'};
    try {
      byte[] btInput = s.getBytes();
      // 获得MD5摘要算法的 MessageDigest 对象
      MessageDigest mdInst = MessageDigest.getInstance("MD5");
      // 使用指定的字节更新摘要
      mdInst.update(btInput);
      // 获得密文
      byte[] md = mdInst.digest();
      // 把密文转换成十六进制的字符串形式
      int j = md.length;
      char str[] = new char[j * 2];
      int k = 0;
      for (int i = 0; i < j; i++) {
        byte byte0 = md[i];
        str[k++] = hexDigits[byte0 >>> 4 & 0xf];
        str[k++] = hexDigits[byte0 & 0xf];
      }
      return new String(str);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Deprecated
  public final static String MD5UP(String s) {
    char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
        'F'};
    try {
      byte[] btInput = s.getBytes();
      // 获得MD5摘要算法的 MessageDigest 对象
      MessageDigest mdInst = MessageDigest.getInstance("MD5");
      // 使用指定的字节更新摘要
      mdInst.update(btInput);
      // 获得密文
      byte[] md = mdInst.digest();
      // 把密文转换成十六进制的字符串形式
      int j = md.length;
      char str[] = new char[j * 2];
      int k = 0;
      for (int i = 0; i < j; i++) {
        byte byte0 = md[i];
        str[k++] = hexDigits[byte0 >>> 4 & 0xf];
        str[k++] = hexDigits[byte0 & 0xf];
      }
      return new String(str);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }


  /**
   * 签名字符串
   *
   * @param text 原来的字符串
   * @param key 加密的 Key
   * @param inputCharset 字符集
   * @return String 签名以后的字符串
   */
  public static String sign(@Nonnull String text, @Nonnull String key, @Nonnull String inputCharset)
      throws UnsupportedEncodingException {
    return DigestUtils.md5Hex(getContentBytes(text + key, inputCharset));
  }


  /**
   * 验证签名以后的字符串
   *
   * @param text 原来的字符串
   * @param sign 已经签名过的字符串
   * @param key 加密的 Key
   * @param inputCharset 字符集
   * @return boolean 结果是否正确
   */
  public static boolean verify(@Nonnull String text, @Nonnull String sign, @Nonnull String key,
      @Nonnull String inputCharset) {
    try {
      String mysign = DigestUtils.md5Hex(getContentBytes(text + key, inputCharset));
      return mysign.equals(sign);
    } catch (UnsupportedEncodingException e) {
      return false;
    }
  }


  /**
   * 根据字符集获取 Bytes
   *
   * @param content 字符串的内容
   * @param charset 字符集
   * @return Bytes Code 以后的内容
   * @throws UnsupportedEncodingException 如果字符集不支持，则会抛出异常
   */
  private static byte[] getContentBytes(@Nonnull String content, @Nonnull String charset)
      throws UnsupportedEncodingException {
    if (charset.isEmpty()) {
      return content.getBytes();
    }

    return content.getBytes(charset);
  }

}
