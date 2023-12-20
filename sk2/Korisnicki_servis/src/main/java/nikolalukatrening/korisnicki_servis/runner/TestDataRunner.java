package nikolalukatrening.korisnicki_servis.runner;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import nikolalukatrening.korisnicki_servis.model.Admin;
import nikolalukatrening.korisnicki_servis.model.User;
import nikolalukatrening.korisnicki_servis.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {

    private AdminRepository adminRepository;

    public TestDataRunner(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Generišete ključ
//        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded()); // Dobićete Base64 kodiran ključ
//        System.out.println("JWT Secret: " + base64Key);

        User user = new User();
        // insert admin
        user.setUsername("softverskekomponente");
        user.setPassword("drugiProjekat"); // Šifra treba da bude enkriptovana
        user.setEmail("softverskekomponente@raf.rs");
        user.setFirstName("Petar");
        user.setLastName("Petrovic");
        user.setDateOfBirth("1970-01-01");
        user.setRole("ROLE_ADMIN");
        Admin admin = new Admin();
        admin.setUser(user); // Setujemo ugnježdenog korisnika

        // Dodavanje ostalih potrebnih polja za Admina ako ih ima
        // admin.setOtherField("OtherValue");

        adminRepository.save(admin);
    }
}
