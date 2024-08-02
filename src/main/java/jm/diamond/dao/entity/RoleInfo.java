package jm.diamond.dao.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity(name = "role_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class RoleInfo {
   /** 롤번호 */
   @Id
   @EqualsAndHashCode.Include
   @Column(name = "roleSeq")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long seq;

   /** 롤명 */
   @Setter
   @Column(name = "roleNm")
   private String name;

   /** 롤설명 */
   @Setter
   @Column(name = "roleDesc")
   private String description;

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

   /** 수정자 */
   @ToString.Exclude
   @ManyToOne(fetch = FetchType.LAZY)
   @Setter
   @JoinColumn(name = "updId")
   private User updateUser;

   /** 수정일시 */
   @Column(name = "updDate")
   @LastModifiedDate
   private LocalDateTime updateDate;

}
