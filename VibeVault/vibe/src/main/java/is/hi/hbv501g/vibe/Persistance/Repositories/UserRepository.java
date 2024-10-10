package is.hi.hbv501g.vibe.Persistance.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.hbv501g.vibe.Persistance.Entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUserName(String userName);

    User findByID(Long id);
}
