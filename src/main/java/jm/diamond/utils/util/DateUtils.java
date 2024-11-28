package jm.diamond.utils.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

   public static final String PATTERN_YYYYMMDD = "yyyyMMdd";

   public static final DateTimeFormatter FORMATTER_YYYYMMDD =
       DateTimeFormatter.ofPattern(PATTERN_YYYYMMDD);

   private DateUtils() {}

   /**
    * 시작 일자를 만들어준다.
    *
    * @param s 일자 값 (최소 연도까지는 입력되어야 함)
    * @return {@link LocalDateTime}
    */
   public static LocalDate makeStartDate(final String s) {
      return DateTimeUtils.makeStartDate(s, PATTERN_YYYYMMDD).toLocalDate();
   }

   /**
    * 종료 일시를 만들어준다.
    *
    * @param s 일시 값 (최소 연도까지는 입력되어야 함)
    * @return
    */
   public static LocalDate makeEndDate(final String s) {
      return DateTimeUtils.makeEndDate(s, PATTERN_YYYYMMDD).toLocalDate();
   }

   public static String format(LocalDate date) {
      return date.format(FORMATTER_YYYYMMDD);
   }

   public static String format(LocalDate date, String pattern) {
      return date.format(DateTimeFormatter.ofPattern(pattern));
   }

   /**
    * 현재 일자를 패턴에 맞춰서 포메팅한다.
    *
    * @param pattern 패턴
    * @return 패턴에 맞춰서 포메팅된 값
    */
   public static String now(String pattern) {
      return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
   }
}
