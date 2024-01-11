package nikolalukatrening.Zakazivanje_servis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDto {

    private Long id;
    private String dateOfHiring;
    private String gymName;
    private UserDto user;

}
