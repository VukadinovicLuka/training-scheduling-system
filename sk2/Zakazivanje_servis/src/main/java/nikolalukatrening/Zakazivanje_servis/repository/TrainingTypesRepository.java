package nikolalukatrening.Zakazivanje_servis.repository;

import nikolalukatrening.Zakazivanje_servis.model.Training;
import nikolalukatrening.Zakazivanje_servis.model.TrainingTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypesRepository extends JpaRepository<TrainingTypes,Long> {
}
