package nikolalukatrening.Zakazivanje_servis.service.impl;


import nikolalukatrening.Zakazivanje_servis.dto.TrainingDto;
import nikolalukatrening.Zakazivanje_servis.mapper.TrainingMapper;
import nikolalukatrening.Zakazivanje_servis.model.Training;
import nikolalukatrening.Zakazivanje_servis.repository.TrainingRepository;
import nikolalukatrening.Zakazivanje_servis.service.TrainingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TrainingServiceImpl implements TrainingService {

    private TrainingRepository trainingRepository;

    private TrainingMapper trainingMapper;
    public TrainingServiceImpl(TrainingRepository trainingRepository,TrainingMapper trainingMapper) {
        this.trainingRepository = trainingRepository;
        this.trainingMapper = trainingMapper;
    }

    @Override
    public Training add(TrainingDto trainingDto) {
        Training training = trainingMapper.trainingDtoToTraining(trainingDto);
        trainingRepository.save(training);
        return training;
    }
}
