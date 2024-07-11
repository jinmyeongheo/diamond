package jm.diamond.dao.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "order_approval_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class OrderApproval {

   @Id
   @Column(name = "orderSeq")
   private String seq;

//   @OneToOne
//   @JoinColumn(name="seq")
//   private Order order;

   private String approvalType;

   private String payTransSerial;

   private LocalDateTime payTransDate;

   private String payTransInfo;

   private String cancelTransSerial;

   private LocalDateTime cancelTransDate;

   private String cancelTransInfo;

   private String cardCode;

   private String cardName;

   private String finCd;

   private String cardNo;

}
