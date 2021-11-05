package mx.edu.utez.keeputez.repository;

import mx.edu.utez.keeputez.model.Category;
import mx.edu.utez.keeputez.model.Note;
import mx.edu.utez.keeputez.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note,Integer> {
    List<Note> findAllByUser(User user);
    List<Note> findAllByCategory(Category category);
}
