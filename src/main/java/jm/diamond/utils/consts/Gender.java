package jm.diamond.utils.consts;

import lombok.Getter;

/** 성별 */
public enum Gender implements LegacyEnum {
   MALE("M"),
   FEMALE("F"),
   /**
    * 모름<br>
    * 포스에서 설정 안 할 수 있음
    */
   UNKNOWN("U");

   @Getter
   private final String code;

   Gender(String code) {
      this.code = code;
   }

   public static Gender of(String code) {
      return LegacyEnum.of(values(), code);
   }
}

