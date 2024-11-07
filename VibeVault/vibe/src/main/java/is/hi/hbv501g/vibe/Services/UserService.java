package is.hi.hbv501g.vibe.Services;

import java.util.Optional;
import is.hi.hbv501g.vibe.Persistance.Entities.User;

public interface UserService {
    void registerUser(User user);
    void updateUser(User user);
    Optional<User> findUserByUsername(String userName);
    Optional<User> findUserById(Long id);
    void deleteUser(Long id);
    void updateUserProfilePicture(String username, String profilePicturePath);
}
