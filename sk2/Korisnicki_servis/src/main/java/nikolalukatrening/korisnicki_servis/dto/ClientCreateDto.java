package nikolalukatrening.korisnicki_servis.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ClientCreateDto {


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

        @NotNull(message = "Card number is required")
        @Min(value = 1, message = "Card number must be greater than 0")
        private Integer cardNumber;

        @NotNull(message = "Reserved training is required")
        @Min(value = 0, message = "Reserved training must be greater than or equal to 0")
        private Integer reservedTraining;


        @Override
        public String toString() {
                return "ClientCreateDto{" +
                        "username='" + username + '\'' +
                        ", password='" + password + '\'' +
                        ", email='" + email + '\'' +
                        ", dateOfBirth='" + dateOfBirth + '\'' +
                        ", firstName='" + firstName + '\'' +
                        ", lastName='" + lastName + '\'' +
                        ", cardNumber=" + cardNumber +
                        ", reservedTraining=" + reservedTraining +
                        '}';
        }
}
