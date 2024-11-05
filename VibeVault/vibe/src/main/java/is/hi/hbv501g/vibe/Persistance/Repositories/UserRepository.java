package is.hi.hbv501g.vibe.Persistance.Repositories;

import is.hi.hbv501g.vibe.Persistance.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;




public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmailAddress(String emailAddress);

}
