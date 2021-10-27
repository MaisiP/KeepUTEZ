package mx.edu.utez.keeputez.repository;

import mx.edu.utez.keeputez.model.Category;
import mx.edu.utez.keeputez.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    List<Category> findAllByUser(User user);
}
