package jm.diamond.dao.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "order_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Order {

   @Id
   @Column(name = "orderSeq")
   private String seq;

//   @ManyToOne
//   @JoinColumn(name = "seq")
//   private PosInfo posInfo;
//
//   @OneToOne(mappedBy = "order")
//   private OrderApproval orderApproval;

   private String payType;

   private BigDecimal amount;

   private String orderState;

   private BigDecimal payReqAmt;

   private BigDecimal cancelAmt;

   private Integer installment;

   @Column(name = "regDate")
   private LocalDateTime regDateTime;

   @Column(name = "updDate")
   private LocalDateTime updDateTime;

   //todo 정적팩토리메소드 활용
   public static Order of(String seq){
      Order build = Order.builder()
          .seq(seq)
          .build();
      return build;
   }

}
