package jm.diamond.service;

import static org.junit.jupiter.api.Assertions.*;

import jm.diamond.dao.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

   @InjectMocks
   private UserService userService;

   @Mock
   private UserRepository userRepository;


   @Test
   void doWork() {
   }
}