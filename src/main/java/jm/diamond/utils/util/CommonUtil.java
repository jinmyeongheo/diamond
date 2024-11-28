package jm.diamond.utils.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jm.diamond.utils.consts.Gender;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public class CommonUtil {
  private CommonUtil() {}

      private static final Logger log = Logger.getLogger(CommonUtil.class.getName());

      /** 결제코드 62진수로 변환 */
      static final char[] BASE62 =
          "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

      // 고정형 결제코드 추출
      public static String getFixedCode(long seq, String regDate) {

      int sum = 0;
      int digitVal = 0;
      String fixedCode = "";
      String tempCode = generateTcode(seq, regDate) + "01"; // 01값은 구분핃드의 초기값

      // 체크 digit 생성
      for (int i = 0; i < tempCode.length(); i++) {
         int temp = Integer.parseInt(tempCode.substring(i, i + 1));
         if (i == 0 || i == 3 || i == 6) {
            sum += temp * 7;
         } else if (i == 1 || i == 4 || i == 7) {
            // sum += temp * 1;
            sum += temp;
         } else if (i == 2 || i == 5 || i == 8) {
            sum += temp * 3;
         }
      }

      digitVal = Math.abs((sum % 10) - 10);
      fixedCode = tempCode + digitVal;
      fixedCode = fixedCode.substring(0, 10);
      return fixedCode;
   }

      // 고정형 결제코드 추출 구분값(type) 2자리 추가
      public static String getFixedCode(long seq, String regDate, String type) {

      int sum = 0;
      int digitVal = 0;
      String fixedCode = "";
      String tempCode = generateTcode(seq, regDate) + type; // 01값은 구분핃드의 초기값

      // 체크 digit 생성
      for (int i = 0; i < tempCode.length(); i++) {
         int temp = Integer.parseInt(tempCode.substring(i, i + 1));
         if (i == 0 || i == 3 || i == 6) {
            sum += temp * 7;
         } else if (i == 1 || i == 4 || i == 7) {
            // sum += temp * 1;
            sum += temp;
         } else if (i == 2 || i == 5 || i == 8) {
            sum += temp * 3;
         }
      }

      digitVal = Math.abs((sum % 10) - 10);
      fixedCode = tempCode + digitVal;
      fixedCode = fixedCode.substring(0, 10);
      return fixedCode;
   }

      /**
       * TCODE 생성
       *
       * @param tranID 일련번호
       * @param tcodeRegDate tcode 발급일시 (YYYYMMDDHHMMSS)
       * @return SRCODE, FORMAT: 7자리, checkparity(1) + scramble number(6)
       */
      public static String generateTcode(long tranID, String tcodeRegDate) {
      if (tranID < 1)
         return null;
      if (tcodeRegDate.length() != 14)
         return null;

      final int[][] intArray = {
          { 4, 1, 3, 9, 8, 2, 7, 5, 0, 6 }, // 첫번째, 000000x
          { 2, 6, 9, 0, 5, 3, 1, 8, 7, 4 }, // 두번째, 00000x0
          { 0, 6, 2, 8, 4, 1, 5, 9, 3, 7 }, // 세번째, 0000x00
          { 5, 4, 2, 1, 9, 6, 7, 0, 8, 3 }, // 네번째, 000x000
          { 5, 3, 4, 7, 0, 9, 2, 6, 8, 1 }, // 다섯째, 00x0000
          { 7, 1, 0, 3, 9, 4, 8, 6, 5, 2 }, // 여섯째, 0x00000
          { 3, 1, 7, 2, 0, 8, 9, 4, 6, 5 }, // 일곱째, x000000
      };

      // int 자릿수 제한
      int n = (int) tranID % 1000000;

      // 일의 자리 수 구하기(배열 뒤섞기 위함)
      int unitOfDigit = n / 1 % 10;
      StringBuilder sb = new StringBuilder();

      for (int i = 0; i < 6; i++) {

         // 자릿수 별 숫자 index 구하기
         int index = n / (int) StrictMath.pow(10, i) % 10;

         // 일의 자리수가 아닌 경우, 일의 자리 수 만큼 배열을 rotate-n-shift
         int[] scrambledArray = intArray[i];
         if (i > 0) {
            scrambledArray = shiftRight(intArray[i], unitOfDigit);
         }

         // 최종 자리수의 값을 구한다
         int digit = scrambledArray[index];

         sb.insert(0, digit);
      }

      String basic = tcodeRegDate + sb.toString();

      int total = 0;
      int prefix;
      try {
         for (int iCnt = 0; iCnt < basic.length(); iCnt++) {
            total += Integer.parseInt(basic.substring(iCnt, iCnt + 1));
         }
         String pos = total + "";
         pos = new StringBuffer(pos).reverse().toString();

         prefix = 0;
         for (int iCnt = 0; iCnt < pos.length(); iCnt++) {
            prefix = Integer.parseInt(pos.substring(iCnt, iCnt + 1));
            if (prefix != 0) break;
         }
         if (prefix == 0) return null; // ERROR 처리
      } catch (Exception e) {
         log.log(Level.WARNING, e.getMessage());
         return null;
      }

      sb.insert(0, prefix);
      return sb.toString();
   }

      /**
       * 오른쪽으로 int 배열을 shift 한다 (generateSrCode 에서 호출)
       *
       * @param a 대상 int형 배열
       * @param i shift 수행 횟수
       * @return 결과 배열
       */
      private static int[] shiftRight(int[] a, int i) {
      return reverse(reverse(reverse(a, 0, a.length), 0, i), i, a.length);
   }

      /**
       * Reverse 수행 함수 (generateSrCode 에서 호출)
       *
       * @param a
       * @param i
       * @param j
       * @return
       */
      private static int[] reverse(int[] a, int i, int j) { // reverse elements i,
      // i+1 ... j-1
      for (; --j > i; i++) {
         int t = a[i];
         a[i] = a[j];
         a[j] = t;
      }
      return a; // convenience.
   }

      // TODO use NFS?

      public static final SecureRandom random = new SecureRandom();

      // SMS 인증번호 추출
      public static String getRandomKey(int len) {
      int nSeed = 0;
      int nSeedSize = 10;
      String strSrc = "0123456789";
      StringBuilder bld = new StringBuilder();
      String strKey = "";

      for (int i = 0; i < len; i++) {
         nSeed = random.nextInt(nSeedSize) + 1;
         bld.append(String.valueOf(strSrc.charAt(nSeed - 1)));
      }
      strKey = bld.toString();

      return strKey;
   }


      public static String enc62To10(long value) {
      final StringBuilder sb = new StringBuilder();
      do {
         int i = (int) (value % 62);
         sb.append(BASE62[i]);
         value /= 62;
      } while (value > 0);
      return sb.toString();
   }

      public static long dec10To62(String value) {
      long result = 0;
      long power = 1;
      for (int i = 0; i < value.length(); i++) {
         int digit = new String(BASE62).indexOf(value.charAt(i));
         result += digit * power;
         power *= 62;
      }
      return result;
   }

      /** apk파일을 업로드하면 해당 apk 파일로 무결성검증값을 도출하도록_2018.10.26 */
      public static String getHashFile(String filePath) throws NoSuchAlgorithmException, IOException {
      try (InputStream input = new FileInputStream(filePath)) {

         byte[] buffer = new byte[1024];
         MessageDigest strHash = MessageDigest.getInstance("SHA-512");
         int numRead = 0;

         while (numRead != -1) {
            numRead = input.read(buffer);
            if (numRead > 0) {
               strHash.update(buffer, 0, numRead);
            }
         }

         byte[] md5Bytes = strHash.digest();

         StringBuilder bld = new StringBuilder();
         String returnVal = "";
         for (int i = 0; i < md5Bytes.length; i++) {
            bld.append(Integer.toString((md5Bytes[i] & 0xff) + 0x100, 16).substring(1));
         }
         returnVal = bld.toString();

         return returnVal.toUpperCase();
      }
   }

      public static String getRandomPassword(int len) {
      char[] charSet =
          new char[] {
              '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
              'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
          };

      int idx = 0;
      StringBuilder bld = new StringBuilder();

      for (int i = 0; i < len; i++) {
         idx = random.nextInt(charSet.length);
         // 36 * 생성된 난수를 Int로 추출 (소숫점제거)
         bld.append(charSet[idx]);
      }

      return bld.toString();
   }

      // 전화번호중 가운데 국번을 마스킹처리
      public static String getPhoneFormatter(String num) {
      String formatNum = "";

      if (num == null || num.equals("")) {
         return num;
      }

      if (num.indexOf("02") == 0) {
         if (num.length() == 10) {
            formatNum = num.substring(0, 2) + "****" + num.substring(6);
         } else if (num.length() == 9) {
            formatNum = num.substring(0, 2) + "***" + num.substring(6);
         } else {
            formatNum = "****" + num.substring(4);
         }

      } else {
         if (num.length() == 11) {
            formatNum = num.substring(0, 3) + "****" + num.substring(7);
         } else if (num.length() == 10) {
            formatNum = num.substring(0, 3) + "***" + num.substring(6);
         } else if (num.length() > 7) {
            formatNum = num.substring(0, 3) + "**" + num.substring(5);
         } else {
            formatNum = "****" + num.substring(4);
         }
      }

      return formatNum;
   }

   /**
    * 차량번호의 뒤 4자리를 추출한다.
    *
    * @param carNumber 차량번호
    * @return 추출된 차량번호
    */
   public static String extractCarNumber(final String carNumber) {
      final String regex = "^(?:[가-힣]{2})?[0-9]{2,3}[가-힣]([0-9]{4})$";
      final Pattern pattern = Pattern.compile(regex);
      final Matcher matcher = pattern.matcher(carNumber);

      if (matcher.find()) {
         return matcher.group(1);
      } else {
         return "";
      }
   }

   /**
    * 주민등록번호 뒤 1자리로 성별을 구한다.<br>
    *
    * @param code 주민등록번호 뒤 1자리
    * @return {@link Gender} 성별
    */
   public static Gender gender(final String code) {
      if (StringUtils.isBlank(code) || code.trim().length() != 1) {
         throw new IllegalArgumentException(code);
      }

      int n = Integer.parseInt(code.trim());
      return n % 2 == 0 ? Gender.FEMALE : Gender.MALE;
   }

   /**
    * 생년월일을 구한다.<br>
    * 성별코드:<br>
    * 1: 1900년대에 태어난 남자<br>
    * 2: 1900년대에 태어난 여자<br>
    * 3: 2000년대에 태어난 남자<br>
    * 4: 2000년대에 태어난 여자<br>
    * 5: 1900년대 태어난 외국인 남자<br>
    * 6: 1900년대에 태어난 외국인 여자<br>
    * 7: 2000년대에 태어난 외국인 남자<br>
    * 8: 2000년대에 태어난 외국인 여자<br>
    * 9: 1800년대에 태어난 남자<br>
    * 0: 1800년대에 태어난 여자
    *
    * @param birthday 6자리 생년월일
    * @param gender 성별 코드(0~9)
    * @return {@link LocalDate} 생년월일
    */
   public static LocalDate birthday(final String birthday, final String gender) {
      if (StringUtils.isBlank(birthday)) {
         throw new IllegalArgumentException(birthday);
      }
      if (StringUtils.isBlank(gender) || gender.trim().length() != 1) {
         throw new IllegalArgumentException(birthday);
      }

      String s = birthday;
      switch (gender) {
         case "1":
         case "2":
         case "5":
         case "6":
            s = "19" + s;
            break;
         case "3":
         case "4":
         case "7":
         case "8":
            s = "20" + s;
            break;
         default:
            s = "18" + s;
            break;
      }
      return LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyyMMdd"));
   }

   /**
    * 임시 비밀번호 생성<br>
    * 생성 규칙: 영문자, 숫자, 특수문자 포함 8자리
    *
    * @param length 생성 비밀번호 길이 (최소 3 이상)
    * @return 임시 비밀번호
    */
   public static String makePassword(int length) {
      Random random = new Random();
      int alphaLength = random.nextInt(length - 2) + 1; // alphaLength: 1 ~ (length - 2)
      int numericLength =
          random.nextInt(length - alphaLength - 1) + 1; // numericLength: 1 ~ (length - 2)
      int specialLength = length - alphaLength - numericLength; // specialLength: 1 ~ (length - 2)
      String alpha = RandomStringUtils.randomAlphabetic(alphaLength);
      String numeric = RandomStringUtils.randomNumeric(numericLength);
      String special =
          RandomStringUtils.random(specialLength, "!\\\"#$%&'()*+,-./:;<=>?@[\\\\]^_`{|}~");
      List<String> target = Arrays.asList((alpha + numeric + special).split(""));
      Collections.shuffle(target);
      StringBuilder password = new StringBuilder();
      for (String letter : target) {
         password.append(letter);
      }
      return password.toString();
   }
}

