package jm.diamond.utils.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class DateTimeUtils {
   private DateTimeUtils() {}

   public static final String PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

   public static final DateTimeFormatter FORMATTER_YYYYMMDDHHMMSS =
       DateTimeFormatter.ofPattern(PATTERN_YYYYMMDDHHMMSS);

   /**
    * 시작 일시를 만들어준다.
    *
    * @param s 일시 값 (최소 연도까지는 입력되어야 함)
    * @param pattern 일시 패턴
    * @return {@link LocalDateTime}
    */
   public static LocalDateTime makeStartDate(final String s, final String pattern) {
      DateTimeFormatter formatter =
          new DateTimeFormatterBuilder()
              .appendPattern(pattern)
              .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
              .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
              .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
              .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
              .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
              .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
              .toFormatter();
      return LocalDateTime.parse(s, formatter);
   }

   /**
    * 종료 일시를 만들어준다.
    *
    * @param s 일시 값 (최소 연도까지는 입력되어야 함)
    * @param pattern 일시 패턴
    * @return {@link LocalDateTime}
    */
   public static LocalDateTime makeEndDate(final String s, final String pattern) {
      DateTimeFormatter formatter =
          new DateTimeFormatterBuilder()
              .appendPattern(pattern)
              .parseDefaulting(ChronoField.MONTH_OF_YEAR, 12)
              .parseDefaulting(ChronoField.DAY_OF_MONTH, 31)
              .parseDefaulting(ChronoField.HOUR_OF_DAY, 23)
              .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 59)
              .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 59)
              .parseDefaulting(ChronoField.NANO_OF_SECOND, 999999)
              .toFormatter();
      return LocalDateTime.parse(s, formatter);
   }

   /**
    * 현재 일시를 패턴에 맞춰서 포멧팅한다.
    *
    * @param pattern 패턴
    * @return {@link String} 포멧팅된 일시
    */
   public static String now(String pattern) {
      return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
   }

   /**
    * 일시를 패턴에 맞춰서 포멧팅한다
    *
    * @param date 일시
    * @param pattern 패턴
    * @return {@link String} 포멧팅된 일시
    */
   public static String format(LocalDateTime date, String pattern) {
      return date.format(DateTimeFormatter.ofPattern(pattern));
   }

   /**
    * 기본 포메터 생성
    *
    * @param pattern 패턴 (미완성 패턴이어도 된다.)
    * @return {@link DateTimeFormatter}
    */
   public static DateTimeFormatter formatter(String pattern) {
      return new DateTimeFormatterBuilder()
          .appendPattern(pattern)
          .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
          .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
          .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
          .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
          .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
          .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
          .toFormatter();
   }
}
