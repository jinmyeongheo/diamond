package jm.diamond.dao.repository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import jm.diamond.dao.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByName(String userName);

    Optional<User> findByEmail(String email);
}



