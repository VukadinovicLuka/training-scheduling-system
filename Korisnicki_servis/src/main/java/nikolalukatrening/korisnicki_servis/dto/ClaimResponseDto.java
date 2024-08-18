package nikolalukatrening.korisnicki_servis.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClaimResponseDto {


    private Integer id;
    private String role;

    public ClaimResponseDto(Integer id, String role) {
        this.id = id;
        this.role = role;
    }
}
