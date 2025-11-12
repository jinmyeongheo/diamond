package jm.diamond.controller.sso;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class TaxiOidcWrapperFactory implements OidcWrapperFactory<TaxiUserDetails> {

    @Override
    public TaxiOidcUserDetailsWrapper createWrapper(TaxiUserDetails userDetails, OidcUser oidcUser) {

        // 커스텀 Wrapper 반환
        return new TaxiOidcUserDetailsWrapper(userDetails,oidcUser);
    }
}
