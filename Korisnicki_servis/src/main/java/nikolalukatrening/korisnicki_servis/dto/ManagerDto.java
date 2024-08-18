package nikolalukatrening.korisnicki_servis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDto {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String dateOfHiring;
    private String gymName;

}
