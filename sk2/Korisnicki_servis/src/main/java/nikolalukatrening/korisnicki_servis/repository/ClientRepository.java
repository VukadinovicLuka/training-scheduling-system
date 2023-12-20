package nikolalukatrening.korisnicki_servis.repository;

import nikolalukatrening.korisnicki_servis.model.Client;
import nikolalukatrening.korisnicki_servis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {

    Optional<Client> findByUserUsername(String username);
    Optional<Client> findByUser_UsernameAndUser_Password(String username, String password);

}
