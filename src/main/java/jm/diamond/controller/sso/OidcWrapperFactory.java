package jm.diamond.controller.sso;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

/**
 * OidcUserDetailsWrapper를 생성하는 팩토리 인터페이스
 *
 * 각 프로젝트에서 구현 선택 가능:
 * - 구현 안 하면: DefaultOidcWrapperFactory 사용
 * - 구현하면: 커스텀 Wrapper 사용
 */
public interface OidcWrapperFactory <T extends UserDetails>{
    /**
     * @param userDetails 프로젝트별 커스텀 UserDetails
     * @param oidcUser Keycloak OIDC 정보
     * @return OidcUser 인터페이스를 구현한 래퍼 객체
     */
    OidcUserDetailsWrapper createWrapper(T userDetails, OidcUser oidcUser);
}
