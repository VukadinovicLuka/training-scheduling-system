package nikolalukatrening.Zakazivanje_servis.repository;

import nikolalukatrening.Zakazivanje_servis.model.Training;
import nikolalukatrening.Zakazivanje_servis.model.TrainingTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingTypesRepository extends JpaRepository<TrainingTypes,Long> {

    List<TrainingTypes> findByTrainingSort(String trainingType);

    TrainingTypes findByTrainingSortAndTrainingType(String trainingSort, String trainingType);
}
