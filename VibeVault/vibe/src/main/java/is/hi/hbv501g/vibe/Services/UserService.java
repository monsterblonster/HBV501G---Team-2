package is.hi.hbv501g.vibe.Services;

import java.util.Optional;
import is.hi.hbv501g.vibe.Persistance.Entities.User;

public interface UserService {
    User registerUser(String userName, String password);
    Optional<User> findUserByUsername(String userName);
    void deleteUser(Long id);
}
