package jm.diamond.dao.repository;

import java.util.List;
import jm.diamond.dao.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

   List<User> findByUserName(String userName);
}
