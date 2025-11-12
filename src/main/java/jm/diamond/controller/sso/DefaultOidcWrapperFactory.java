package jm.diamond.controller.sso;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@Slf4j
public class DefaultOidcWrapperFactory implements OidcWrapperFactory<UserDetails> {

    @Override
    public OidcUserDetailsWrapper createWrapper(UserDetails userDetails, OidcUser oidcUser) {
        log.warn("팩토리 설정해주세요.");
        return new DefaultOidcUserDetailsWrapper(userDetails, oidcUser);
    }
}
