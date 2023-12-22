package nikolalukatrening.GUI2.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientProfileEditorDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("cardNumber")
    private Integer cardNumber;

    @JsonProperty("reservedTraining")
    private Integer reservedTraining;

    @JsonProperty("isActivated")
    private Boolean isActivated;

    @JsonProperty("activationToken")
    private String activationToken;

    @JsonProperty("user")
    private UserDto user;

    // getters and setters

@Getter
@Setter
public class UserDto {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("email")
    private String email;

    @JsonProperty("dateOfBirth")
    private String dateOfBirth;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("role")
    private String role;
    }
    // getters and setters
}


