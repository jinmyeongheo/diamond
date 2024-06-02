package jm.diamond.security;

import java.util.Optional;
import jm.diamond.dao.entity.User;
import jm.diamond.dao.repository.UserRepository;
import jm.diamond.security.UserDto.UserDtoBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            User user1 = user.get();
            UserDto userDto = new UserDtoBuilder()
                .seq(user1.getSeq())
                .loginId(user1.getEmail())
                .password(passwordEncoder.encode(user1.getPw()))
                .role("admin")
                .build();
            return userDto;
        }else{
            throw new UsernameNotFoundException("아이디찾기실패");
        }
    }
}
