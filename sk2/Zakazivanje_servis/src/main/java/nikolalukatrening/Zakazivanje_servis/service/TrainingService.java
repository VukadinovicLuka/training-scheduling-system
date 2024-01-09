package nikolalukatrening.Zakazivanje_servis.service;

import nikolalukatrening.Zakazivanje_servis.dto.TrainingDto;
import nikolalukatrening.Zakazivanje_servis.model.Training;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {


    Training add(TrainingDto trainingDto);

    Training update(TrainingDto trainingDto);

    Training updateReserve(TrainingDto trainingDto);
}
