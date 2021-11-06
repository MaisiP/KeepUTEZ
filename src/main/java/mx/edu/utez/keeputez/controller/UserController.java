package mx.edu.utez.keeputez.controller;

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
    public ResponseEntity<?> getPreferences() {
        User user = getUserInSession();
        if (user == null)
            return Utils.getResponseEntity("Usuario no encontrado", HttpStatus.BAD_REQUEST);
        return Utils.getResponseEntityObject(notificationRepository.findNotificationByUser(user), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("notification")
    public ResponseEntity<?> savePreferences(@DTO(NotificationCreateDTO.class) Notification notification) {
        User user = getUserInSession();
        if (user == null)
            return Utils.getResponseEntity("Usuario no encontrado", HttpStatus.BAD_REQUEST);
        notificationRepository.deleteAllByUser(user);
        notification.setUser(user);
        notificationRepository.save(notification);
        return Utils.getResponseEntity("Preferencias guargadas", HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("notification")
    public ResponseEntity<?> deletePreferences(@DTO(NotificationDeleteDTO.class) Notification notification) {
        if (notification == null)
            return Utils.getResponseEntity("Preferencias no encontradas", HttpStatus.BAD_REQUEST);
        notificationRepository.delete(notification);
        return Utils.getResponseEntity("Preferencias eliminadas", HttpStatus.OK);
    }

    @GetMapping("category")
    public ResponseEntity<?> findCategories() {
        User user = getUserInSession();
        return Utils.getResponseEntityList(categoryRepository.findAllByUser(user), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("category")
    public ResponseEntity<?> saveCategory(@DTO(CategoryCreateDTO.class) Category category) {
        User user = getUserInSession();
        category.setUser(user);
        categoryRepository.save(category);
        return Utils.getResponseEntity("Categoria guardada", HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("category")
    public ResponseEntity<?> deleteCategory(@DTO(CategoryDeleteDTO.class) Category category) {
        if (categoryRepository.existsById(category.getId())) {
            List<Note> notes = noteRepository.findAllByCategory(category);
            notes.forEach(note -> note.setCategory(null));
            categoryRepository.deleteById(category.getId());
            return Utils.getResponseEntity("Categoria eliminada", HttpStatus.OK);
        } else {
            return Utils.getResponseEntity("Categoria no encontrada", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("note")
    public ResponseEntity<?> findNotes() {
        User user = getUserInSession();
        if (user == null)
            return Utils.getResponseEntity("Usuario no encontrado", HttpStatus.BAD_REQUEST);
        return Utils.getResponseEntityList(noteRepository.findAllByUser(user), HttpStatus.OK);
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

    @Transactional
    @DeleteMapping("note")
    public ResponseEntity<?> deleteNote(@DTO(NoteDeleteDTO.class) Note note) {
        if(note == null){
             return Utils.getResponseEntity("Nota no encontrada", HttpStatus.BAD_REQUEST);
        }
        noteRepository.delete(note);
        return Utils.getResponseEntity("Nota eliminada", HttpStatus.OK);
    }

    private User getUserInSession() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserByUsername(user.getUsername());
    }
}
