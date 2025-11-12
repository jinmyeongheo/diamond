package jm.diamond.controller.sso;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 기본 Wrapper 구현체
 */
public class DefaultOidcUserDetailsWrapper implements OidcUserDetailsWrapper {

    private final UserDetails userDetails;
    private final OidcUser oidcUser;

    // 파라미터 제네릭으로 userDetails를 구현한 클래스만 받는걸로 처리해도 될듯?
    public DefaultOidcUserDetailsWrapper(UserDetails userDetails, OidcUser oidcUser) {
        this.userDetails = userDetails;
        this.oidcUser = oidcUser;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OidcUserDetailsWrapper.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Map<String, Object> getClaims() {
        return Collections.emptyMap();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
