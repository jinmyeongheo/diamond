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
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Entity(name = "privilegeInfo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PrivilegeInfo {

   /** 권한 ID */
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @EqualsAndHashCode.Include
   @Column(name = "privilegeSeq")
   private Long seq;

   /** 권한 명칭 */
   @Setter
   @Column(name = "privilegeNm")
   private String name;

   /** 권한 설명 */
   @Setter
   @Column(name = "privilegeDesc")
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
   @LastModifiedBy
   @JoinColumn(name = "updId")
   private User updateUser;

   /** 수정일시 */
   @LastModifiedDate
   @Column(name = "updDate")
   private LocalDateTime updateDate;



}
