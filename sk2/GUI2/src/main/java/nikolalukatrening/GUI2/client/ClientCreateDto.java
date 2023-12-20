package nikolalukatrening.GUI2.client;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCreateDto {

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

    @JsonProperty("cardNumber")
    private Integer cardNumber;

    @JsonProperty("reservedTraining")
    private Integer reservedTraining;

    public ClientCreateDto(String username, String password, String email, String dateOfBirth, String firstName, String lastName, Integer cardNumber, Integer reservedTraining) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cardNumber = cardNumber;
        this.reservedTraining = reservedTraining;
    }
}
