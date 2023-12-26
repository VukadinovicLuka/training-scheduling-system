package nikolalukatrening.Zakazivanje_servis.dto;


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


    @Override
    public String toString() {
        return "ClientProfileEditorDto{" +
                "id=" + id +
                ", cardNumber=" + cardNumber +
                ", reservedTraining=" + reservedTraining +
                ", isActivated=" + isActivated +
                ", activationToken='" + activationToken + '\'' +
                ", user=" + user +
                '}';
    }
}


