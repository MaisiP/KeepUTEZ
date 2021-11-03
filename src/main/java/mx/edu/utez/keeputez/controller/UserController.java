package mx.edu.utez.keeputez.controller;

import mx.edu.utez.keeputez.bean.ErrorMessage;
import mx.edu.utez.keeputez.bean.SuccessMessage;
import mx.edu.utez.keeputez.model.Category;
import mx.edu.utez.keeputez.model.Note;
import mx.edu.utez.keeputez.model.Notification;
import mx.edu.utez.keeputez.model.User;

import mx.edu.utez.keeputez.model.dto.NoteCreateDTO;
import mx.edu.utez.keeputez.model.dto.NoteUpdateDTO;
import mx.edu.utez.keeputez.repository.CategoryRepository;
import mx.edu.utez.keeputez.repository.NotificationRepository;
import mx.edu.utez.keeputez.repository.NoteRepository;
import mx.edu.utez.keeputez.repository.UserRepository;
import mx.edu.utez.keeputez.util.DTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/")
public class UserController {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NoteRepository noteRepository;

    public UserController(CategoryRepository categoryRepository, UserRepository userRepository,
                          NotificationRepository notificationRepository, mx.edu.utez.keeputez.repository.NoteRepository noteRepository) {
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
    public SuccessMessage savePreferences(Notification notification) {
        User user = getUserInSession();
        notification.setUser(user);
        notificationRepository.save(notification);
        return new SuccessMessage("Configuración de preferencias guardada");
    }

    @DeleteMapping("notification")
    public Object deletePreferences(Notification notification) {
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
    public SuccessMessage saveCategory(Category category) {
        User user = getUserInSession();
        category.setUser(user);
        categoryRepository.save(category);
        return new SuccessMessage("Categoria guardada");
    }

    @DeleteMapping("category")
    public Object deleteCategory(Category category) {
        if (categoryRepository.existsById(category.getId())) {
            List<Note> notes = noteRepository.findAllByCategory(category);
            for (Note note : notes) {
                note.setCategory(null);
                noteRepository.save(note);
            }
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

    //Update
    @PutMapping("note")
    public SuccessMessage updateNote(@DTO(NoteUpdateDTO.class) Note note) {
        noteRepository.save(note);
        return new SuccessMessage("Nota guardada");
    }

    @DeleteMapping("note")
    public Object deleteNote(Note note) {
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
