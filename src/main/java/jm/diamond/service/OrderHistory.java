package jm.diamond.service;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderHistory {

   private BigDecimal amt;

   private String affName;

}
