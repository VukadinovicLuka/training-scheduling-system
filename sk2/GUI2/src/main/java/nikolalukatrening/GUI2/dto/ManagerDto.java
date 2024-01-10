package nikolalukatrening.GUI2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
