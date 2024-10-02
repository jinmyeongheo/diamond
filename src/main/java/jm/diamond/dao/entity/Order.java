package jm.diamond.dao.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Order_Info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Order {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

//   @ManyToOne
//   @JoinColumn(name = "seq")
//   private PosInfo posInfo;
//
//   @OneToOne(mappedBy = "order")
//   private OrderApproval orderApproval;

   @Column(name = "user_id")
   private Long userId;
   @Column(name = "order_date")
   private LocalDateTime orderDate;
   @Column(name = "total_amount")
   private BigDecimal totalAmount;
   @Column(name = "status")
   private String status;
   @Column(name = "payment_method")
   private String paymentMethod;

//   private BigDecimal amount;
//
//   private String orderState;
//
//   private BigDecimal payReqAmt;
//
//   private BigDecimal cancelAmt;
//
//   private Integer installment;
//
//   @Column(name = "regDate")
//   private LocalDateTime regDateTime;
//
//   @Column(name = "updDate")
//   private LocalDateTime updDateTime;

   //todo 정적팩토리메소드 활용
   public static Order of(Long seq){
      Order build = Order.builder()
          .id(seq)
          .build();
      return build;
   }

//   public void plusPayReqAmt(){
//      this.payReqAmt.add(BigDecimal.ONE);
//   }

}
