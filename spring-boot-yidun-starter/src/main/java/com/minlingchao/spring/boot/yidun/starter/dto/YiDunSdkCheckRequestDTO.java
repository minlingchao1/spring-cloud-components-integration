package com.minlingchao.spring.boot.yidun.starter.dto;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: sdk端check请求
 * @date 2018/11/08 7:11 PM
 */
public class YiDunSdkCheckRequestDTO {

  /**
   * 反作弊结果查询token，由业务前端页面提交给业务后端
   */
  private String token;

  /**
   * 用户唯一标识，如果是手机号or邮箱，请提供hash值，hash算法：md5(account)
   */
  private String account;

  /**
   * 用户的邮箱hash，hash算法：md5(email)
   */
  private String email;

  /**
   * 用户的手机号hash，hash算法：md5(phone)
   */
  private String phone;

  /**
   * 用户参加活动时的ip
   */
  private String ip;

  /**
   * 用户的注册时间，单位：秒
   */
  private Long registerTime;

  /**
   * 用户的注册IP
   */
  private String registerIp;

  /**
   * 活动的唯一标识
   */
  private String activityId;

  /**
   * 活动操作的目标，比如：A给B点赞，则target为B。如果target是手机号或邮箱，请提供hash值，hash算法：md5(target)
   */
  private String target;

  /**
   * 额外信息，建议自己构造json结构
   */
  private String extData;
}
