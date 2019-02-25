package com.minlingchao.spring.boot.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: 密码校验
 * @date 2018/11/12 3:32 PM
 */

public class PasswordValidator implements ConstraintValidator<Password, String> {

  private static final Integer MIN_LENGTH = 8;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("密码不能为空")
          .addConstraintViolation();
      return false;
    } else {
      //判断密码长度
      if (value.length() < MIN_LENGTH) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("密码长度不能小于8位")
            .addConstraintViolation();
        return false;
      } else {
        //判断是否只有数字/只有字母
        if (StringUtils.isNumeric(value) || StringUtils.isAlpha(value)) {
          context.disableDefaultConstraintViolation();
          context.buildConstraintViolationWithTemplate("密码必须包含数字、字母")
              .addConstraintViolation();
          return false;
        }
      }
    }
    return true;
  }
}
