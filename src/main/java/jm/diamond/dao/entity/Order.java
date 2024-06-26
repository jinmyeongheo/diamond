package jm.diamond.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Order {

   @Id
   private String seq;

   @ManyToOne
   private PosInfo posInfo;

   @OneToOne
   private OrderApproval orderApproval;

}
