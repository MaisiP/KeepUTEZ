package mx.edu.utez.keeputez.repository;

import mx.edu.utez.keeputez.model.Notification;
import mx.edu.utez.keeputez.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Integer> {
    Notification findNotificationByUser(User user);
    void deleteAllByUser(User user);
}
