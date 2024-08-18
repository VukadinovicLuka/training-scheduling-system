package nikolalukatrening.korisnicki_servis.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientUpdateDto {

    private String oldUsername;
    private String username;
    @Email
    private String email;
    private String firstName;
    private String lastName;

}
