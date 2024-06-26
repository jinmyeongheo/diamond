package jm.diamond.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PosInfo {

   @Id
   private Long seq;

   private String posName;

}
