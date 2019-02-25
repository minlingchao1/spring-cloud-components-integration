package com.minlingchao.spring.boot.common.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: 手机号校验
 * @date 2018/11/12 3:16 PM
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      //禁用默认提示信息
      context.disableDefaultConstraintViolation();
      //设置提示语
      context.buildConstraintViolationWithTemplate("手机号码不能为空")
          .addConstraintViolation();
      return false;
    } else {
      String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";

      Pattern p = Pattern.compile(regex);
      Matcher m = p.matcher(value);
      boolean match = m.matches();
      if (!match) {
        //禁用默认提示信息
        context.disableDefaultConstraintViolation();
        //设置提示语
        context.buildConstraintViolationWithTemplate("无效的手机号码")
            .addConstraintViolation();
        return false;
      }
      return true;
    }
  }
}
