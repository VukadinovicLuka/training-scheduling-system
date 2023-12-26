package nikolalukatrening.Zakazivanje_servis.mapper;

import nikolalukatrening.Zakazivanje_servis.dto.TrainingDto;
import nikolalukatrening.Zakazivanje_servis.model.Training;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {

    public Training trainingDtoToTraining(TrainingDto trainingDto){
        Training training = new Training();
        training.setTrainingType(trainingDto.getTrainingType());
        training.setIsGroupTraining(trainingDto.getIsGroupTraining());
        training.setDate(trainingDto.getDate());
        training.setStartTime(trainingDto.getStartTime());
        training.setGym(null);
        training.setMaxParticipants(trainingDto.getMaxParticipants());
        return training;
    }

}
