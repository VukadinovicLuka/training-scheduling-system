package nikolalukatrening.GUI2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TrainingDto {

    private LocalDate date;
    private Boolean isGroupTraining;
    private Integer maxParticipants;
    private String startTime;
    private String trainingType;
    private Integer gymId;
    private Integer userId;


}
