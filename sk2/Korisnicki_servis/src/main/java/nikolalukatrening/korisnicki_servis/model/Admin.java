package nikolalukatrening.korisnicki_servis.model;

import jakarta.persistence.*;

@Entity
@Table(indexes = {@Index(columnList = "username", unique = true), @Index(columnList = "password", unique = true)})

public class Admin {
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincrement
    private Long id;

    @Embedded
    private User user;
}
