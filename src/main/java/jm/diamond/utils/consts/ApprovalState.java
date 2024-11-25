package jm.diamond.utils.consts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApprovalState implements LegacyEnum{

   APPLY("0", "접수"),
   APPROVAL("1", "승인완료");


   private final String code;
   private final String desc;

   public static ApprovalState of(String code) {
      return LegacyEnum.of(values(), code);
   }

}
