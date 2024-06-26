package jm.diamond.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class OrderApproval {

   @Id
   private String seq;

   @OneToOne
   private Order order;

   private String payMethod;

}
