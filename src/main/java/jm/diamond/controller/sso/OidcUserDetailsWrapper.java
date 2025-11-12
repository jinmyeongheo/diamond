package jm.diamond.controller.sso;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

/**
 * OidcUser, UserDetails에 모두 사용가능한 인터페이스
 */
public interface OidcUserDetailsWrapper extends OidcUser, UserDetails {
}
