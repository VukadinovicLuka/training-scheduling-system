package nikolalukatrening.korisnicki_servis.repository;

import nikolalukatrening.korisnicki_servis.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
}
