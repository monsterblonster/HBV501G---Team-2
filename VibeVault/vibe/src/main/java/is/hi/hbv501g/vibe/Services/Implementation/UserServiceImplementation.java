package is.hi.hbv501g.vibe.Services.Implementation;

import is.hi.hbv501g.vibe.Persistance.Entities.User;
import is.hi.hbv501g.vibe.Persistance.Repositories.UserRepository;
import is.hi.hbv501g.vibe.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(String userName, String password) {
        User user = new User(); // Create a new User object
        user.setUserName(userName); // Set the username using the setter
        user.setUserPW(password); // Set the password using the setter
        userRepository.save(user); // Save to the repository
    }


    @Override
    public Optional<User> findUserByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
