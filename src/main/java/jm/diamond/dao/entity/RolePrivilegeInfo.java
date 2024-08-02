package jm.diamond.dao.entity;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

@Entity(name = "role_privilege_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class RolePrivilegeInfo {
   /** 역할 일련 번호 */
   @Id
   @NonNull
   @EqualsAndHashCode.Include
   @ToString.Exclude
   @ManyToOne(
       fetch = FetchType.LAZY,
       cascade = {CascadeType.MERGE, CascadeType.PERSIST})
   @JoinColumn(name = "roleSeq")
   private RoleInfo role;

   /** 권한 ID */
   @Id
   @NonNull
   @EqualsAndHashCode.Include
   @ToString.Exclude
   @ManyToOne(
       fetch = FetchType.EAGER,
       cascade = {CascadeType.MERGE, CascadeType.PERSIST})
   @JoinColumn(name = "privilegeSeq")
   private PrivilegeInfo privilege;

   /** 등록자 */
   @ToString.Exclude
   @ManyToOne(fetch = FetchType.LAZY)
   @Setter
   @JoinColumn(name = "regId")
   private User registrationUser;

   /** 등록일시 */
   @Column(name = "regDate")
   @CreatedDate
   private LocalDateTime registrationDate;
}
