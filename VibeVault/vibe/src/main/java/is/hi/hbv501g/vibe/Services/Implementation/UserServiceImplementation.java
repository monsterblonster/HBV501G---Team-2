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
    public void registerUser(User user) {
        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        if (userRepository.findByEmailAddress(user.getEmailAddress()).isPresent()) {
            throw new IllegalArgumentException("Email address is already registered.");
        }

        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
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
