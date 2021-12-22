package com.yl.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：jerry
 * @date ：Created in 2021/12/21 16:42
 * @description：LocalDate时间工具类
 * @version: V1.1
 */
public class TimeUtil {
    /** todo LocalDateTime说明：
     *   time="2018-12-30T19:34:50.63"   ----->转LocalDateTime  ,LocalDateTime.parse();
     *  LocalDateTime年份增加，---->plusYears()
     *  LocalDateTime月份增加，---->plusMonths()
     *  LocalDateTime天数增加，---->plusDays()
     *  LocalDateTime小时增加，---->plusHours()
     *  LocalDateTime分钟数增加，---->plusMinutes()
     *   LocalDateTime周数增加，---->plusWeeks()
     * */

    final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    final  static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * 方法用途：yyyy/MM/dd转LocalDate
     * todo LocalDate转LocalDateTime用atStartOfDay()方法
     * */
    public static LocalDate yyyyMMddReviseLocalDate(String yyyyMMdd) throws ParseException {
        Date parse = new SimpleDateFormat("yyyy/MM/dd").parse(yyyyMMdd);
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(parse);
        LocalDate parse1 = LocalDate.parse(dateString, dateTimeFormatter);
        return parse1;
    }

    /**
     * 方式用途：LocalDate转Date
     * */
    public static Date localDateReviseDate(LocalDate localDate){
        Date date = Date.from(localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
        return date;
    }

    /**
     * 方式用途：LocalDateTime转Date
     * */
    public static Date localDateTimeReviseDate(LocalDateTime localDateTime){
        Date date = Date.from(localDateTime.atZone(ZoneOffset.ofHours(8)).toInstant());
        return date;
    }

    /**
     * 方法用途：Date转LocalDate
     * */
    public static LocalDate dateReviseLocalDate(Date date){
        LocalDate localDate = date.toInstant().atZone(ZoneOffset.ofHours(8)).toLocalDate();
        return localDate;
    }

    /**
     * 方法用途：Date转LocalDateTime
     * */
    public static LocalDateTime dateReviseLocalDateTime(Date date){
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        return localDateTime;
    }

    /**
     * 方法用途：LocalDate转时间戳
     * */
    public static long localDateRevisetTimestamp(LocalDate localDate){
        long timestamp = localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
        return timestamp;
    }

    /**
     * 方法用途：LocalDateTime转时间戳
     * */
    public static long localDateTimeRevisetTimestamp(LocalDateTime localDateTime){
        long timestamp = localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        return timestamp;
    }
    /**
     * 方法用途：时间戳转LocalDate
     * */
    public static LocalDate timestampRevisetLocalDate(long timestamp){
        LocalDate localDate = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDate();
        return localDate;
    }

    /**
     * 方法用途：时间戳转LocalDateTime
     * */
    public static LocalDateTime timestampRevisetLocalDateTime(long timestamp){
        LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        return localDateTime;
    }

    /**
     * 方法用途：获取当日的开始结束时间以成字符串的形式输出
     * */
    public static  Map<Object, String> startTimeAndEndTime(){
        Map<Object, String> map = new HashMap<>();
        //当天的开始时间
        LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        map.put("startTime",df.format(today_start));
        //当天结束时间
        LocalDateTime today_end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        map.put("endTime",df.format(today_end));
        return map;
    }

    /**
     * 常用案例展示
     * */
    public void timeTest(){
        LocalDate localDate = LocalDate.now();
        //当前时间基础上，指定本年当中的第几天，取值范围为1-365,366
        LocalDate withDayOfYearResult = localDate.withDayOfYear(200);
        //当前时间基础上，指定本月当中的第几天，取值范围为1-29,30,31
        LocalDate withDayOfMonthResult = localDate.withDayOfMonth(5);
        //当前时间基础上，直接指定年份
        LocalDate withYearResult = localDate.withYear(2017);
        //当前时间基础上，直接指定月份
        LocalDate withMonthResult = localDate.withMonth(5);

        LocalTime localTime = LocalTime.now();
        //当前时间基础上，直接指定小时
        LocalTime withHourResult = localTime.withHour(1);
        //当前时间基础上，直接指定分钟
        LocalTime withMinuteResult = localTime.withMinute(15);
        //当前时间基础上，直接指定秒
        LocalTime withSecondResult = localTime.withSecond(20);


        // 本月第一天0:00时刻:
        LocalDateTime firstDay = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        // 本月最后1天:
        LocalDate lastDay = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        // 下月第1天:
        LocalDate nextMonthFirstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
        // 本月第1个周一:
        LocalDate firstWeekday = LocalDate.now().with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));


        LocalDateTime localDateTime = LocalDateTime.now();
        //以下方法的参数都是long型，返回值都是LocalDateTime
        LocalDateTime plusYearsResult = localDateTime.plusYears(2L);
        LocalDateTime plusMonthsResult = localDateTime.plusMonths(3L);
        LocalDateTime plusDaysResult = localDateTime.plusDays(7L);
        LocalDateTime plusHoursResult = localDateTime.plusHours(2L);
        LocalDateTime plusMinutesResult = localDateTime.plusMinutes(10L);
        LocalDateTime plusSecondsResult = localDateTime.plusSeconds(10L);


        //也可以以另一种方式来相加减日期，即plus(long amountToAdd, TemporalUnit unit)
        //                  参数1 ： 相加的数量， 参数2 ： 相加的单位
        LocalDateTime nextMonth = localDateTime.plus(1, ChronoUnit.MONTHS);
        LocalDateTime nextYear = localDateTime.plus(1, ChronoUnit.YEARS);
        LocalDateTime nextWeek = localDateTime.plus(1, ChronoUnit.WEEKS);

        //时间相减
        LocalDateTime beforeMonth = localDateTime.minus(1, ChronoUnit.MONTHS);
        LocalDateTime beforeYear = localDateTime.minus(1, ChronoUnit.YEARS);
        LocalDateTime beforeWeek = localDateTime.minus(1, ChronoUnit.WEEKS);
    }

    public static void main(String[] args) throws ParseException {
//        LocalDate localDate = yyyyMMddReviseLocalDate("2019/7/15");
//        System.out.println(localDate);
//
//        Date date = localDateReviseDate(LocalDate.now());
//        System.out.println(date);
        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime plus = now.plusYears(1);
//        LocalDateTime localDateTime = now.plusMonths(10L);
//        LocalDateTime localDateTime = now.plusHours(1);
//        LocalDateTime localDateTime = now.plusMinutes(10L);
        LocalDateTime localDateTime = now.plusWeeks(1);
        System.out.println(localDateTime);

        LocalDateTime nextYear = localDateTime.plus(1, ChronoUnit.YEARS);
        System.out.println(nextYear);
    }
}
