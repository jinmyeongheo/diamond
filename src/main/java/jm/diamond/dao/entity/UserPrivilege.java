package jm.diamond.dao.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "userPrivilege")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPrivilege {

    @EmbeddedId
    private UserPrivilegeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userSeq")  // PK의 userSeq와 매핑
    @JoinColumn(name = "userSeq", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("privilegeSeq")  // PK의 privilegeSeq와 매핑
    @JoinColumn(name = "privilegeSeq", nullable = false)
    private PrivilegeInfo privilege;

    @Column(name = "granted_at")
    private LocalDateTime grantedAt;

    @Builder
    public UserPrivilege(User user, PrivilegeInfo privilege, LocalDateTime grantedAt) {
        this.user = user;
        this.privilege = privilege;
        this.id = new UserPrivilegeId(user.getSeq(), privilege.getSeq());
        this.grantedAt = grantedAt != null ? grantedAt : LocalDateTime.now();
    }
}
