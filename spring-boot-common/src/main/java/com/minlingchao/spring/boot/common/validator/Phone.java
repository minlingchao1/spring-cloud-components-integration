package com.minlingchao.spring.boot.common.validator;

/**
 * @author minlingchao
 * @version V1.0
 * @Description:
 * @date 2018/11/12 3:15 PM
 */

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {PhoneValidator.class})
public @interface Phone {

  String message() default "手机号校验错误";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
