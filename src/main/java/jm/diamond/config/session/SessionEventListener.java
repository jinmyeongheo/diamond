package jm.diamond.config.session;

import org.springframework.context.event.EventListener;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionDestroyedEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.stereotype.Component;

@Component
public class SessionEventListener {

    /**
     * 반드시 Indexed Repository를 사용해야 한다.
     * Spring에서는 여러 방법으로 애플리케이션 이벤트를 수신할 수 있지만, 여기서는 @EventListener를 사용
     * */

    @EventListener
    public void processSessionCreatedEvent(SessionCreatedEvent event) {
        // 세션 생성 시 처리
    }

    @EventListener
    public void processSessionDeletedEvent(SessionDeletedEvent event) {
        // 세션 삭제 시 처리
    }

    @EventListener
    public void processSessionDestroyedEvent(SessionDestroyedEvent event) {
        // 세션 종료 시 처리
    }

    @EventListener
    public void processSessionExpiredEvent(SessionExpiredEvent event) {
        // 세션 만료 시 처리
    }
}

