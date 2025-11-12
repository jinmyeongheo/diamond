package jm.diamond.controller.sso;

import jm.diamond.dao.entity.User;
import jm.diamond.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaxiUserLoadService implements UserLoadService <TaxiUserDetails> {
    private final UserRepository userRepository;

    @Override
    public TaxiUserDetails loadUser(String username) {
        List<User> byName = userRepository.findByName(username);
        // 권한 및 롤 설정.
        // TaxiUserDetails는 각 프로젝트에서 사용하던 UserDetailsService구현체를 사용한다.
        return new TaxiUserDetails();
    }
}

