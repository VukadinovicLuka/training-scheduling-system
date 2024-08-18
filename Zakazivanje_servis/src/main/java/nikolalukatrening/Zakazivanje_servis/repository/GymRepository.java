package nikolalukatrening.Zakazivanje_servis.repository;

import nikolalukatrening.Zakazivanje_servis.model.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GymRepository extends JpaRepository<Gym,Long> {
    Optional<Gym> findByName(String gymName);
}
