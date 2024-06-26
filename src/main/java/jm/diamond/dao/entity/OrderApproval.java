package jm.diamond.dao.entity;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Setter;

@Entity
public class OrderApproval {

   @Id
   private String seq;

   @OneToOne
   private Order order;

   private String payMethod;

}
