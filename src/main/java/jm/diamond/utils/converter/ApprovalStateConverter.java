package jm.diamond.utils.converter;

import jm.diamond.utils.consts.ApprovalState;
import org.springframework.core.convert.converter.Converter;

public class ApprovalStateConverter implements Converter<String, ApprovalState> {

   @Override
   public ApprovalState convert(String s) {
      return ApprovalState.of(s);
   }
}
