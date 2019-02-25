package com.minlingchao.spring.boot.dynamic.datasource.starter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author minlingchao
 * @version 1.0
 * @date 2017/11/17 上午10:19
 * @description
 */
@Configuration
@Import(DynamicDataSourceRegister.class)
@ComponentScan(basePackages = {"com.minlingchao.spring.boot.dynamic.datasource.starter"})
public class DynamicDataSourceAutoConfiguration {
}
