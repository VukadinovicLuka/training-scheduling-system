package nikolalukatrening.Notifikacioni_servis.controller;

import nikolalukatrening.Notifikacioni_servis.model.NotificationType;
import nikolalukatrening.Notifikacioni_servis.repository.NotificationRepository;
import nikolalukatrening.Notifikacioni_servis.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
//    private NotificationService service;
    private NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
//        this.service = service;
        this.notificationRepository = notificationRepository;
    }


    // Dodajte end-pointe za definisanje, ažuriranje, brisanje i listanje tipova notifikacija

    @GetMapping("/{id}")
    public ResponseEntity<List<NotificationType>> getNotificaitonsByClientId(@PathVariable Long id) {
        List<NotificationType> notificationTypeOptional = notificationRepository.findByClientId(id);
        return notificationTypeOptional.isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(notificationTypeOptional);


    }
}
