package nikolalukatrening.Zakazivanje_servis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GymDto {
    private Long id;
    private String name;
    private String description;
    private Integer personalTrainerNumbers;
}
