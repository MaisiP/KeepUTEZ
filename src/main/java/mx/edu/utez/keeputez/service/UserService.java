package mx.edu.utez.keeputez.service;

import mx.edu.utez.keeputez.model.User;
import mx.edu.utez.keeputez.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private UserService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user != null) return user;
        else throw new UsernameNotFoundException("Username not found");
    }
}
