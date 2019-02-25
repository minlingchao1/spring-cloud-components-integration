package com.minlingchao.spring.boot.dynamic.datasource.starter;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author minlingchao
 * @version V1.0
 * @Description:
 * @date 2019/02/25 17:24
 */
public class DynamicDataSourceAnnotationAdvisor extends AbstractPointcutAdvisor implements
    BeanFactoryAware {

  private Advice advice;

  private Pointcut pointcut;

  public DynamicDataSourceAnnotationAdvisor(
      DynamicDataSourceAnnotationInterceptor dynamicDataSourceAnnotationInterceptor) {
    this.advice = dynamicDataSourceAnnotationInterceptor;
    this.pointcut = buildPointcut();
  }

  @Override
  public Pointcut getPointcut() {
    return this.pointcut;
  }

  @Override
  public Advice getAdvice() {
    return this.advice;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    if (this.advice instanceof BeanFactoryAware) {
      ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
    }
  }

  private Pointcut buildPointcut() {
    Pointcut cpc = (Pointcut) new AnnotationMatchingPointcut(TargetDataSource.class, true);
    // 类注解
    Pointcut clpc = (Pointcut) AnnotationMatchingPointcut
        .forClassAnnotation(TargetDataSource.class);
    // 方法注解
    Pointcut mpc = (Pointcut) AnnotationMatchingPointcut
        .forMethodAnnotation(TargetDataSource.class);
    return new ComposablePointcut(cpc).union(clpc).union(mpc);
  }

}
