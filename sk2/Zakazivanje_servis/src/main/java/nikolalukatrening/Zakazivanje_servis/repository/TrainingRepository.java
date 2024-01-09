package nikolalukatrening.Zakazivanje_servis.repository;

import nikolalukatrening.Zakazivanje_servis.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingRepository extends JpaRepository<Training,Long> {

        List<Training> findByDate(LocalDate date);
        List<Training> findAllByDate(LocalDate date);
        Optional<Training> findByDateAndStartTime(LocalDate date, String startTime);

        Optional<Training> findByDateAndStartTimeAndMaxParticipants(LocalDate date, String startTime, Integer i);
        List<Training> findAllByUserId(Long userId);

        Optional<Training> findByDateAndStartTimeAndUserId(LocalDate date, String startTime, Long i);
}
