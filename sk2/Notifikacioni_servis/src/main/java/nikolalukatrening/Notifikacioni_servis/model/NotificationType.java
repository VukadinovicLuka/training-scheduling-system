package nikolalukatrening.Notifikacioni_servis.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

@Entity
public class NotificationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincrement
    private Long id;
    private String type;
    private String firstName;
    private String lastName;
    private String link;
    private String receiver;
    private String password;

    // Konstruktori, getteri i setteri
}

