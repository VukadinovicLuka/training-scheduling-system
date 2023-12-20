package nikolalukatrening.korisnicki_servis.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class User {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String dateOfBirth;

    private String role;
}
