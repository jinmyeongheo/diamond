package jm.diamond.dao.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class OrderInfo {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private BigDecimal amount;

   private String paymentMethod;

   private String status;

   private LocalDateTime orderDateTime;

   //todo 정적팩토리메소드 활용
   public static OrderInfo of(Long seq){
      OrderInfo build = OrderInfo.builder()
          .id(seq)
          .build();
      return build;
   }


}
