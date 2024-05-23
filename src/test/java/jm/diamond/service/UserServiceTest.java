package jm.diamond.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jm.diamond.dao.entity.User;
import jm.diamond.dao.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
   @Mock
   private UserRepository userRepository;

   @InjectMocks
   private UserServiceImpl userService;



   @Test
   void doWork() {
      List<User> users = new ArrayList<>();
      users.add(new User(111l,"jinmyeong.heo@seeroo.co.kr","허진명", "1234"));

      when(userRepository.findByUserName("허진명")).thenReturn(users);
      when(userRepository.findById(1494l)).thenReturn(Optional.of(new User(123l,"1@1.com","하이", "1234")));
      User result = userService.doWork();

      Assertions.assertEquals(users.get(0), result);


   }
}