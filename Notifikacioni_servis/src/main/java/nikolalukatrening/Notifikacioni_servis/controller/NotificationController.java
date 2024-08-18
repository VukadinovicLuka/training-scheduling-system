package nikolalukatrening.Notifikacioni_servis.controller;

import nikolalukatrening.Notifikacioni_servis.model.NotificationType;
import nikolalukatrening.Notifikacioni_servis.repository.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/manager/{id}")
    public ResponseEntity<List<NotificationType>> getNotificaitonsByManagerId(@PathVariable Long id) {
        List<NotificationType> notificationTypeOptional = notificationRepository.findByManagerId(id);
        return notificationTypeOptional.isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(notificationTypeOptional);
    }

    @GetMapping("/all")
    public ResponseEntity<List<NotificationType>> getAllNotifications() {
        List<NotificationType> notifications = notificationRepository.findAll();
        return notifications.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(notifications);
    }
    @GetMapping("/type")
    public ResponseEntity<List<NotificationType>> getNotificationsByType(@RequestParam String notificationType) {
        List<NotificationType> notifications = notificationRepository.findByType(notificationType);
        if (notifications.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(notifications);
        }
    }

    // Inside NotificationController class:

    @GetMapping("/receiver")
    public ResponseEntity<List<NotificationType>> getNotificationsByEmail(@RequestParam String receiver) {
        List<NotificationType> notifications = notificationRepository.findByReceiver(receiver);
        if (notifications.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notifications);
    }
}
