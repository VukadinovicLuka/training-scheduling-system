package nikolalukatrening.Notifikacioni_servis.repository;

import nikolalukatrening.Notifikacioni_servis.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationType,Long> {
    List<NotificationType> findByClientId(Long clientId);
    List<NotificationType> findByType(String type);
    List<NotificationType> findByReceiver(String receiver);
}
