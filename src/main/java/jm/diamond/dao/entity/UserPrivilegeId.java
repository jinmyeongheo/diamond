package jm.diamond.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPrivilegeId implements Serializable {

    private Long userSeq;
    private Long privilegeSeq;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPrivilegeId)) return false;
        UserPrivilegeId that = (UserPrivilegeId) o;
        return Objects.equals(userSeq, that.userSeq) &&
                Objects.equals(privilegeSeq, that.privilegeSeq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userSeq, privilegeSeq);
    }
}
