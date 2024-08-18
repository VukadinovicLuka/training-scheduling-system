package nikolalukatrening.korisnicki_servis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(indexes = {@Index(columnList = "cardNumber", unique = true), @Index(columnList = "username", unique = true), @Index(columnList = "password", unique = true)})

public class Client {
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincrement
    private Long id;
    private Integer cardNumber;
    private Integer reservedTraining;
    private Boolean isActivated;
    private String activationToken;
    @Embedded
    private User user;

}
