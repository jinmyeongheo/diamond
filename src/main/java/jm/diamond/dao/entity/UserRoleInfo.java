package jm.diamond.dao.entity;

import java.time.LocalDateTime;
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
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Entity(name = "user_role_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class UserRoleInfo {
   /** 사용자 일련 번호 */
   @Id
   @NonNull
   @EqualsAndHashCode.Include
   @ToString.Exclude
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "seq")
   private User user;

   /** 역할 일련 번호 */
   @Id
   @NonNull
   @EqualsAndHashCode.Include
   @ToString.Exclude
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "roleSeq")
   private RoleInfo role;

   /** 등록자 */
   @ToString.Exclude
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "regId")
   @CreatedBy
   private User registrationUser;

   /** 등록일시 */
   @Column(name = "regDate")
   @CreatedDate
   private LocalDateTime registrationDate;

   /** 수정자 */
   @ToString.Exclude
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "updId")
   @LastModifiedBy
   private User updateUser;

   /** 수정일시 */
   @Column(name = "updDate")
   @LastModifiedDate
   private LocalDateTime updateDate;
}
