package com.minlingchao.spring.boot.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: 日期工具类
 * @date 2018/10/19 9:11 PM
 */
public class DateUtil {

  /**
   * 一天
   */
  public static final long DAY_MILLS = TimeUnit.DAYS.toMillis(1);

  /**
   * 一小时
   */
  public static final long ONE_HOUR_MILLS = TimeUnit.HOURS.toMillis(1);

  /**
   * 一周
   */
  public static final long ONE_WEEK_MILLS = TimeUnit.DAYS.toMillis(7);

  /**
   * 一分钟
   */
  public static final long ONE_MINUTE_MILLS = TimeUnit.MINUTES.toMillis(1);

  private static ZoneId zone = ZoneId.systemDefault();

  /**
   * 字符串转Date
   */
  public static Date convertToDate(String date) throws Exception {
    LocalDate localDate = null;
    if (null == date) {
      throw new NullPointerException("date isn't null");
    } else {
      localDate = LocalDate.parse(date);
      return convertToDate(localDate);
    }
  }

  /**
   * 字符串转LocalDateTime
   *
   * @return localDateTime
   */
  public static LocalDateTime convertToLocalDateTime(String date) {
    LocalDateTime localDateTime = null;
    if (null == date) {
      throw new NullPointerException("date isn't null");
    } else {
      localDateTime = LocalDateTime.parse(date);
      return localDateTime;
    }
  }

  /**
   * LocalDate转Date
   *
   * @return Date
   */
  public static Date convertToDate(LocalDate localDate) {
    Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
    return Date.from(instant);
  }

  /**
   * LocalDateTime转Date
   *
   * @return Date
   */
  public static Date convertToDate(LocalDateTime localDateTime) {
    Instant instant = localDateTime.atZone(zone).toInstant();
    return Date.from(instant);
  }

  /**
   * Date转LocalDate
   *
   * @return localDate
   */
  public static LocalDate convertToLocalDate(Date date) {
    Instant instant = date.toInstant();
    return convertToLocalDateTime(instant).toLocalDate();
  }

  /**
   * Date转LocalTime
   *
   * @return localDate
   */
  public static LocalTime convertToLocalTime(Date date) {
    Instant instant = date.toInstant();
    return convertToLocalDateTime(instant).toLocalTime();
  }


  /**
   * Date转LocalDatetime
   *
   * @return localDate
   */
  public static LocalDateTime convertToLocalDateTime(Date date) {
    Instant instant = date.toInstant();
    return convertToLocalDateTime(instant);
  }

  /**
   * LocalDateTime转Instant
   */
  public static Instant convertToInstant(LocalDateTime localDateTime) {
    return localDateTime.atZone(zone).toInstant();
  }

  /**
   * LocalDate转Instant
   */
  public static Instant convertToInstant(LocalDate localDate) {
    return localDate.atStartOfDay(zone).toInstant();
  }

  /**
   * LocalDate转LocalDateTime
   *
   * @return LocalDateTime
   */
  public static LocalDateTime convertToLocalDateTime(LocalDate localDate) {
    return localDate.atStartOfDay();
  }

  /**
   * 日周期格式化
   */
  public static String formatter(LocalDateTime localDateTime, String formatStyle) {
    return DateTimeFormatter.ofPattern(formatStyle).format(localDateTime);
  }

  /**
   * 设置年
   *
   * @return LocalDateTime
   */
  public static LocalDateTime setYear(LocalDateTime sourceDate, Integer year) {
    return sourceDate.withYear(year);
  }

  /**
   * 设置天
   *
   * @return LocalDateTime
   */
  public static LocalDateTime setDayOfMonth(LocalDateTime sourceDate, Integer dayOfMonth) {
    return sourceDate.withDayOfMonth(dayOfMonth);
  }


  /**
   * 设置月
   *
   * @return LocalDateTime
   */
  public static LocalDateTime setMonth(LocalDateTime sourceDate, Integer month) {
    return sourceDate.withMonth(month);
  }

  /**
   * 设置小时
   */
  public static LocalDateTime setHour(LocalDateTime sourceDate, Integer hour) {
    return sourceDate.withHour(hour);

  }

  /**
   * 设置分钟
   */
  public static LocalDateTime setMinute(LocalDateTime sourceDate, Integer minute) {
    return sourceDate.withMinute(minute);
  }

  /**
   * 设置秒
   */
  public static LocalDateTime setSecond(LocalDateTime sourceDate, Integer second) {
    return sourceDate.withSecond(second);
  }

  /**
   * 修改年月日
   */
  public static LocalDateTime setYMD(LocalDateTime sourceDate, Integer year, Integer month,
      Integer dayOfMonth) {
    return sourceDate.withYear(year).withMonth(month).withDayOfMonth(dayOfMonth);
  }

  /**
   * 修改时分秒
   */
  public static LocalDateTime setHMS(LocalDateTime sourceDate, Integer hour, Integer minute,
      Integer second) {
    return sourceDate.withHour(hour).withMinute(minute).withSecond(second);
  }

  /**
   * 计算相差的天数
   */
  public static int getInteverDays(LocalDate beginDate, LocalDate endDate) {
    Period period = Period.between(beginDate, endDate);
    return period.getDays();
  }

  /**
   * 计算相差的年数
   */
  public static int getInteverYears(LocalDate beginDate, LocalDate endDate) {
    Period period = Period.between(beginDate, endDate);
    return period.getYears();
  }

  /**
   * 日期加减
   */
  @SuppressWarnings("static-access")
  public static LocalDate addLocalDate(long num, ChronoUnit unit, final LocalDate localDate) {
    LocalDate resultDate;
    if (num > 0) {
      resultDate = LocalDate.now().plus(num, unit);
    } else {
      resultDate = LocalDate.now().minus(Math.abs(num), unit);
    }
    return resultDate;
  }

  /**
   * 日期时分秒加
   */
  @SuppressWarnings("static-access")
  public static LocalDateTime addLocalDateTime(long num, ChronoUnit unit) {
    LocalDateTime resultDateTime;
    if (num > 0) {
      resultDateTime = LocalDateTime.now().plus(num, unit);
    } else {
      resultDateTime = LocalDateTime.now().minus(Math.abs(num), unit);
    }
    return resultDateTime;
  }

  /**
   * 时分秒加减
   */
  @SuppressWarnings("static-access")
  public static LocalTime addLocalTime(long num, ChronoUnit unit) {
    LocalTime resultTime;
    if (num > 0) {
      resultTime = LocalTime.now().plus(num, unit);
    } else {
      resultTime = LocalTime.now().minus(Math.abs(num), unit);
    }
    return resultTime;
  }

  /**
   * Instant转LocalDateTime
   */
  public static LocalDateTime convertToLocalDateTime(Instant instant) {
    return LocalDateTime.ofInstant(instant, zone);
  }

  /**
   * 判断是否是当前年份
   *
   * @param localDateTime 指定日期
   * @return 是否是当前年份
   */
  public static boolean isCurrentYear(LocalDateTime localDateTime) {
    return localDateTime.getYear() == LocalDateTime.now().getYear();
  }

  /**
   * 获取指定日期是否是当前月份
   *
   * @param localDateTime 指定日期
   * @return 是否是当前月份
   */
  public static boolean isCurrentMonth(LocalDateTime localDateTime) {
    LocalDateTime now = LocalDateTime.now();
    return localDateTime.getYear() == now.getYear()
        && localDateTime.getMonthValue() == now.getMonthValue();
  }

  /**
   * 获取指定日期是否是当前这天
   *
   * @param localDateTime 指定日期
   * @return 是否是当前这天
   */
  public static boolean isCurrentDay(LocalDateTime localDateTime) {
    LocalDateTime now = LocalDateTime.now();
    return localDateTime.getYear() == now.getYear()
        && localDateTime.getMonthValue() == now.getMonthValue()
        && localDateTime.getDayOfMonth() == now.getDayOfMonth();
  }

  public static long currentMills() {
    return LocalDateTime.now().atZone(zone).toInstant().toEpochMilli();
  }
}
