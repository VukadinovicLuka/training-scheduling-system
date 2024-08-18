package nikolalukatrening.Notifikacioni_servis.repository;

import nikolalukatrening.Notifikacioni_servis.model.EmailMessage;
import nikolalukatrening.Notifikacioni_servis.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailMessageRepository extends JpaRepository<NotificationType,Long> {
}
