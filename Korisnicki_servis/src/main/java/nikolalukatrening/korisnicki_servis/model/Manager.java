package nikolalukatrening.korisnicki_servis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(indexes = {@Index(columnList = "gymName", unique = true), @Index(columnList = "username", unique = true), @Index(columnList = "password", unique = true)})

public class Manager {
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincrement
    private Long id;
    private String dateOfHiring;
    private String gymName;

    @Embedded
    private User user;
}
