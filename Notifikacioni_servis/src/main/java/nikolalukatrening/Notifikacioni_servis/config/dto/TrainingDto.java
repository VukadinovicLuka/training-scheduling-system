package nikolalukatrening.Notifikacioni_servis.config.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TrainingDto {

    private LocalDate date;
    private Boolean isGroupTraining;
    private Integer maxParticipants;
    private String startTime;
    private String trainingType;
    private Integer gymId;
    private Integer userId;
    private Boolean isAvailable;

}
