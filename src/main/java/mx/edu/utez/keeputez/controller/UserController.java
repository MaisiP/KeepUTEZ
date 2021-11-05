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
import mx.edu.utez.keeputez.util.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    private SuccessMessage updateListNotes(@DTO(CategoryUpdateListNotes.class) Category category) {
        categoryRepository.save(category);
        return new SuccessMessage("Lista actualizada");
    }

    @Transactional
    @DeleteMapping("category")
    public Object deleteCategory(@DTO(CategoryDeleteDTO.class) Category category) {
        if (categoryRepository.existsById(category.getId())) {
            List<Note> notes = noteRepository.findAllByCategory(category);
            notes.forEach(note -> note.setCategory(null));
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
    public ResponseEntity<?> saveNote(@DTO(NoteCreateDTO.class) Note note) {
        User user = getUserInSession();
        note.setUser(user);
        noteRepository.save(note);
        return Utils.getResponseEntity("Nota guardada", HttpStatus.OK);
    }

    @Transactional
    @PutMapping("note")
    public ResponseEntity<?> updateNote(@Valid @RequestBody NoteUpdateDTO noteDTO) {
        Note note = noteRepository.findById(noteDTO.getId()).orElse(null);
        if (note != null) {
            note.setTitle(noteDTO.getTitle());
            note.setBody(noteDTO.getBody());
            if (noteDTO.getCategory() == null) {
                note.setCategory(null);
            } else {
                Category category = categoryRepository.findById(noteDTO.getCategory().getId()).orElse(null);
                note.setCategory(category);
            }
            return Utils.getResponseEntity("Nota actualizada", HttpStatus.OK);
        } else {
            return Utils.getResponseEntity("Nota no encontrada", HttpStatus.BAD_REQUEST);
        }
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
