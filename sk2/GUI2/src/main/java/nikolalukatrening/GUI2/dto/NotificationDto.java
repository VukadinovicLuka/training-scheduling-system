package nikolalukatrening.GUI2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDto {

    private Long id;
    private String type;
    private String firstName;
    private String lastName;
    private String link;
    private String receiver;
    private String password;
    private Long clientId;

    @Override
    public String toString() {
        return "NotificationDto{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", link='" + link + '\'' +
                ", receiver='" + receiver + '\'' +
                ", password='" + password + '\'' +
                ", clientId=" + clientId +
                '}';
    }
}
