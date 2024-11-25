package jm.diamond.utils.consts;

public interface LegacyEnum {
   /**
    * 데이터베이스 값
    *
    * @return
    */
   String getCode();

   static <E extends Enum<E> & LegacyEnum> E of(E[] values, String code) {
      for (E e : values) {
         if(e.getCode() == null && e.getCode() == code){  // NONE이 있는 Enum은 equals()에서 NullException 발생
            return e;
         }
         else if (e.getCode() != null && e.getCode().equals(code)) {
            return e;
         }
      }
      return null;
   }
}
