package nikolalukatrening.korisnicki_servis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class ClientAdminDto {

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
}

// getters and setters

