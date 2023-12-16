package nikolalukatrening.korisnicki_servis.repository;

import nikolalukatrening.korisnicki_servis.model.Admin;
import nikolalukatrening.korisnicki_servis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
}
