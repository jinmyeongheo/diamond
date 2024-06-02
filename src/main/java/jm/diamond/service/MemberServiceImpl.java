package jm.diamond.service;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import jm.diamond.dao.entity.User;
import jm.diamond.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User doWork() {
        List<User> users = userRepository.findByName("허진명");
        System.out.println("user = " + users.get(0).getName());

        Optional<User> byId = userRepository.findById(1494l);
        String userName = byId.get().getName();
        System.out.println(userName);

        return users.get(0);
    }

    @Override
    @Transactional
    public void registerMember(User user) {
        user.encryptPassword(bCryptPasswordEncoder.encode(user.getPw()));
        userRepository.save(user);
    }
}
