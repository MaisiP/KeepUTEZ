package mx.edu.utez.keeputez.controller;

import mx.edu.utez.keeputez.model.User;
import mx.edu.utez.keeputez.model.dto.UserCreateDTO;
import mx.edu.utez.keeputez.repository.UserRepository;
import mx.edu.utez.keeputez.util.DTO;
import mx.edu.utez.keeputez.util.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/public/")
public class PublicController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PublicController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @PostMapping("user")
    public ResponseEntity<?> saveUser(@DTO(UserCreateDTO.class) User user){
        if (userRepository.existsByUsername(user.getUsername()))
            return Utils.getResponseEntity("Nombre de usuario en uso", HttpStatus.BAD_REQUEST);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return Utils.getResponseEntity("Usuario registrado",HttpStatus.OK);
    }
}
