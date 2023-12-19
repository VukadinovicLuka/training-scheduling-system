package nikolalukatrening.Notifikacioni_servis.repository;

import nikolalukatrening.Notifikacioni_servis.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {
    NotificationType findByName(String name);
}
