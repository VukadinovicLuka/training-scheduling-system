package nikolalukatrening.korisnicki_servis.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class ManagerCreateDto {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "DateOfBirth is required")
    private String dateOfBirth;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of hiring is required")
    private String dateOfHiring;

    @NotNull(message = "Gym name is required")
    private String gymName;


}
