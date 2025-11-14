package jm.diamond.config.session;

import lombok.RequiredArgsConstructor;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final FindByIndexNameSessionRepository<? extends Session> sessions;


    /**
     * Indexed Repository 덕분에 “principalName” 기반 세션 인덱싱이 가능하고, 이를 활용해 세션 관리 기능을 구현할 수 있다.
     *
     **/


    /*
    * * getSessions → 특정 사용자의 모든 세션을 조회
    * */
    public Collection<? extends Session> getSessions(Principal principal) {
        Collection<? extends Session> usersSessions =
                this.sessions.findByPrincipalName(principal.getName()).values();
        return usersSessions;
    }

    /**
     * removeSession → 사용자의 특정 세션만 골라서 삭제
     * */
    public void removeSession(Principal principal, String sessionIdToDelete) {
        Set<String> usersSessionIds =
                this.sessions.findByPrincipalName(principal.getName()).keySet();
        if (usersSessionIds.contains(sessionIdToDelete)) {
            this.sessions.deleteById(sessionIdToDelete);
        }
    }

}
