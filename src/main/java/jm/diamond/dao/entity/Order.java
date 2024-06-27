package jm.diamond.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
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
   private String seq;

   @ManyToOne
   private PosInfo posInfo;

   @OneToOne
   private OrderApproval orderApproval;

   //todo 정적팩토리메소드 활용
   public static Order of(String seq){
      Order build = Order.builder()
          .seq(seq)
          .build();
      return build;
   }

}
