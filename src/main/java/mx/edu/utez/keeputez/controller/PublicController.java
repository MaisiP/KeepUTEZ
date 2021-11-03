package mx.edu.utez.keeputez.controller;

import mx.edu.utez.keeputez.bean.ErrorMessage;
import mx.edu.utez.keeputez.bean.SuccessMessage;
import mx.edu.utez.keeputez.model.User;
import mx.edu.utez.keeputez.model.dto.UserCreateDTO;
import mx.edu.utez.keeputez.repository.UserRepository;
import mx.edu.utez.keeputez.util.DTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/")
public class PublicController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PublicController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("user")
    public Object saveUser(@DTO(UserCreateDTO.class) User user){
        if (userRepository.existsByUsername(user.getUsername()))
            return new ErrorMessage("Nombre de usuario en uso");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new SuccessMessage("Usuario registrado");
    }
}
