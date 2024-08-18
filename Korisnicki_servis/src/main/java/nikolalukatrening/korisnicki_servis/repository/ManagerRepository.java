package nikolalukatrening.korisnicki_servis.repository;

import nikolalukatrening.korisnicki_servis.model.Admin;
import nikolalukatrening.korisnicki_servis.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager,Long> {
    Optional<Manager> findByUserUsername(String username);

    Optional<Manager> findByGymName(String gymName);
}
