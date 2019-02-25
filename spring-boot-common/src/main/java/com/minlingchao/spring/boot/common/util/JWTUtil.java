package com.minlingchao.spring.boot.common.util;

import com.minlingchao.spring.boot.common.dto.CreateJWTRequestDTO;
import com.minlingchao.spring.boot.common.dto.CreateJwtResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: jwt 工具类
 * @date 2018/10/22 11:32 AM
 */
public class JWTUtil {

  /**
   * 解析jwt token
   */
  public static Claims parseJWT(String token, String jwtSecret) {
    SecretKey key = generalKey(jwtSecret);
    return Jwts.parser()
        .setSigningKey(key)
        .parseClaimsJws(token)
        .getBody();
  }

  public static CreateJwtResponseDTO createJWT(CreateJWTRequestDTO createJWTRequestDTO)
      throws Exception {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    long nowMills = DateUtil.currentMills();

    Date now = new Date(nowMills);

    SecretKey key = generalKey(createJWTRequestDTO.getSecret());

    JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
        .setSubject(GsonUtil.toJson(createJWTRequestDTO.getJwtInfo()))
        .setIssuedAt(now)
        .signWith(signatureAlgorithm, key);

    if (createJWTRequestDTO.getExpireInMills() > 0) {
      long expireAtMills = nowMills + createJWTRequestDTO.getExpireInMills();
      Date expireAt = new Date(expireAtMills);
      builder.setExpiration(expireAt).setNotBefore(now);
      String token = builder.compact();

      return CreateJwtResponseDTO.builder()
          .token(token)
          .expireAt(expireAtMills)
          .build();
    } else {
      throw new Exception("生成token出错");
    }
  }

  /**
   * 由字符串生成加密key
   */
  private static SecretKey generalKey(String jwtSecret) {
    byte[] encodedKey = Base64.decodeBase64(jwtSecret);
    SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    return key;
  }

}
