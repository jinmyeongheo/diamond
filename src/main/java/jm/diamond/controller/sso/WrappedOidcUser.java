package jm.diamond.controller.sso;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class WrappedOidcUser implements OidcUser, UserDetails {
    
    private final DefaultOidcUser delegateOidcUser;
    private final UserDetails delegateUserDetails;

    // OidcUser
    @Override
    public Map<String, Object> getClaims() {
        return delegateOidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return delegateOidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return delegateOidcUser.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegateOidcUser.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegateOidcUser.getAuthorities();
    }

    // UserDetails
    
    @Override
    public String getPassword() {
        return delegateUserDetails.getPassword();
    }

    @Override
    public String getUsername() {
        return delegateUserDetails.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return delegateUserDetails.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return delegateUserDetails.isAccountNonLocked();
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
    public String getName() {
        return delegateOidcUser.getName();
    }
}
