package jm.diamond.controller.sso;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserLoadService <T extends UserDetails> {
    T loadUser(String username);
}
