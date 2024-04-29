package jm.diamond.service;

import java.util.List;
import java.util.Optional;
import jm.diamond.dao.entity.User;
import jm.diamond.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;

   @Override
   public void doWork() {
      List<User> users = userRepository.findByUserName("허진명");
      System.out.println("user = " + users.get(0).getUserName());

      Optional<User> byId = userRepository.findById(1494l);
      String userName = byId.get().getUserName();
      System.out.println(userName);
   }
}
