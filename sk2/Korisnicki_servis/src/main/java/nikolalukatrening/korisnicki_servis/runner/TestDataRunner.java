package nikolalukatrening.korisnicki_servis.runner;

import nikolalukatrening.korisnicki_servis.model.Admin;
import nikolalukatrening.korisnicki_servis.model.User;
import nikolalukatrening.korisnicki_servis.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {

    private AdminRepository adminRepository;

    public TestDataRunner(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setUsername("softverskekomponente");
        user.setPassword("drugiProjekat"); // Šifra treba da bude enkriptovana
        user.setEmail("softverskekomponente@raf.rs");
        user.setFirstName("Petar");
        user.setLastName("Petrovic");
        user.setDateOfBirth("1970-01-01");

        Admin admin = new Admin();
        admin.setUser(user); // Setujemo ugnježdenog korisnika

        // Dodavanje ostalih potrebnih polja za Admina ako ih ima
        // admin.setOtherField("OtherValue");

        adminRepository.save(admin);
    }
}
