package mx.edu.utez.keeputez.controller;

import mx.edu.utez.keeputez.bean.ErrorMessage;
import mx.edu.utez.keeputez.bean.SuccessMessage;
import mx.edu.utez.keeputez.model.Category;
import mx.edu.utez.keeputez.model.Note;
import mx.edu.utez.keeputez.model.Notification;
import mx.edu.utez.keeputez.model.User;

import mx.edu.utez.keeputez.model.dto.*;
import mx.edu.utez.keeputez.repository.CategoryRepository;
import mx.edu.utez.keeputez.repository.NotificationRepository;
import mx.edu.utez.keeputez.repository.NoteRepository;
import mx.edu.utez.keeputez.repository.UserRepository;
import mx.edu.utez.keeputez.util.DTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user/")
public class UserController {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NoteRepository noteRepository;

    public UserController(CategoryRepository categoryRepository, UserRepository userRepository,
                          NotificationRepository notificationRepository, NoteRepository noteRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.noteRepository = noteRepository;
    }

    @GetMapping("notification")
    public Notification getPreferences() {
        User user = getUserInSession();
        return notificationRepository.findNotificationByUser(user);
    }

    @PostMapping("notification")
    public SuccessMessage savePreferences(@DTO(NotificationCreateDTO.class) Notification notification) {
        User user = getUserInSession();
        notification.setUser(user);
        notificationRepository.save(notification);
        return new SuccessMessage("Configuración de preferencias guardada");
    }

    @DeleteMapping("notification")
    public Object deletePreferences(@DTO(NotificationDeleteDTO.class) Notification notification) {
        notificationRepository.deleteById(notification.getId());
        if (notificationRepository.existsById(notification.getId())) {
            return new SuccessMessage("Preferencias eliminadas");
        } else {
            return new ErrorMessage("Preferencias no eliminadas");
        }
    }

    @GetMapping("category")
    public List<Category> findCategories() {
        User user = getUserInSession();
        return categoryRepository.findAllByUser(user);
    }

    @PostMapping("category")
    public SuccessMessage saveCategory(@DTO(CategoryCreateDTO.class) Category category) {
        User user = getUserInSession();
        category.setUser(user);
        categoryRepository.save(category);
        return new SuccessMessage("Categoria guardada");
    }

    @PutMapping("category")
    private SuccessMessage updateListNotes(@DTO(CategoryUpdateListNotes.class) Category category){

        categoryRepository.save(category);
        return new SuccessMessage("Lista actualizada");
    }

    @DeleteMapping("category")
    public Object deleteCategory(@DTO(CategoryDeleteDTO.class) Category category) {
        if (categoryRepository.existsById(category.getId())) {

            categoryRepository.deleteById(category.getId());
            return new SuccessMessage("Categoria eliminada");
        } else {
            return new ErrorMessage("Categoria no encontrada");
        }
    }

    @GetMapping("note")
    public List<Note> findNotes() {
        User user = getUserInSession();
        return noteRepository.findAllByUser(user);
    }

    @PostMapping("note")
    public SuccessMessage saveNote(@DTO(NoteCreateDTO.class) Note note) {
        User user = getUserInSession();
        note.setUser(user);
        noteRepository.save(note);
        return new SuccessMessage("Nota guardada");
    }

    @PutMapping("note")
    public SuccessMessage updateNote(@DTO(NoteUpdateDTO.class) Note note) {
        noteRepository.save(note);
        return new SuccessMessage("Nota actualizada");
    }

    @DeleteMapping("note")
    public Object deleteNote(@DTO(NoteDeleteDTO.class) Note note) {
        categoryRepository.deleteById(note.getId());
        if (categoryRepository.existsById(note.getId())) {
            return new SuccessMessage("Nota eliminada");
        } else {
            return new ErrorMessage("Nota no eliminada");
        }
    }

    private User getUserInSession() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserByUsername(user.getUsername());
    }
}
