package jm.diamond.dao.entity;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long seq;

   private String name;

   /** 이메일 */
   private String email;

   private String pw;

   @OneToMany(mappedBy = "seq")
   private List<PrivilegeInfo> privilegeInfos;

   public void encryptPassword(String encryptPassword){
      this.pw = encryptPassword;
   }
}
