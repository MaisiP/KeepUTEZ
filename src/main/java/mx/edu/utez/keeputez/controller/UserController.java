package mx.edu.utez.keeputez.controller;

import mx.edu.utez.keeputez.model.Notification;
import mx.edu.utez.keeputez.model.User;

import mx.edu.utez.keeputez.repository.CategoryRepository;
import mx.edu.utez.keeputez.repository.NotificationRepository;
import mx.edu.utez.keeputez.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final mx.edu.utez.keeputez.repository.NoteRepository noteRepository;

    public UserController(CategoryRepository categoryRepository, UserRepository userRepository,
                          NotificationRepository notificationRepository, mx.edu.utez.keeputez.repository.NoteRepository noteRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.noteRepository = noteRepository;
    }

    public Notification getPreferences(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return notificationRepository.findNotificationByUser(user);
    }
}
