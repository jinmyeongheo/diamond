package jm.diamond.controller.sso;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class LoginUser implements UserDetails {

    private final String name;
    private final List<String> authorities;
    private final String password;

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
}
