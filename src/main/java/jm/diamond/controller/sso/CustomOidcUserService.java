package jm.diamond.controller.sso;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

/**
 * OIDC 인증 후 UserDetails로 변환하는 커스텀 서비스
 *
 * 동작 방식:
 * 1. Keycloak에서 OidcUser 정보 가져오기
 * 2. UserLoadService로 db에 있는 유저정보 가져오기 UserDetails로
 * 3. OidcUserDetailsWrapper로 감싸서 반환
 */
@Component
@RequiredArgsConstructor
public class CustomOidcUserService <T extends UserDetails> extends OidcUserService {

    // 각 서비스별로 유저를 호출하는 서비스를 호출, 인터페이스를 구현하지않은면 exception 발생.
    // 필수: 각 프로젝트에서 반드시 구현
    private final UserLoadService<T> userLoadService;

    // 선택: 구현 안 하면 DefaultOidcWrapperFactory 사용
    private final OidcWrapperFactory<T> wrapperFactory;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. Keycloak에서 OIDC 사용자 정보 로드
        OidcUser oidcUser = super.loadUser(userRequest);

        // 2. username 추출 (Keycloak의 preferred_username 또는 email)
        String username = extractUsername(oidcUser);

        // 3. DB 에서 T(UserDetails 구현체) 조회(각 프로젝트 구현체)
        T userDetails = userLoadService.loadUser(username);

        // 4. Factory로 Wrapper 생성 (OCP 준수!)
        //    - 기본: OidcUserDetailsWrapper
        //    - 커스텀: 프로젝트별 구현체
        return wrapperFactory.createWrapper(userDetails, oidcUser);
    }

    /**
     * Keycloak에서 username 추출
     */
    private String extractUsername(OidcUser oidcUser) {
        String username = oidcUser.getPreferredUsername();
        if (username == null || username.isEmpty()) {
            username = oidcUser.getEmail();
        }
        if (username == null || username.isEmpty()) {
            throw new OAuth2AuthenticationException("사용자 식별 정보를 찾을 수 없습니다.");
        }
        return username;
    }
}

