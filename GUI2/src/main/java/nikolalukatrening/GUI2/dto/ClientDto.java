package nikolalukatrening.GUI2.dto;


// ClientDto bi trebao da sadrži informacije o korisniku koje su sigurne za deljenje preko klijentsko-server komunikacije.
// ali bez osetljivih informacija kao što su lozinke ili bilo koji drugi privatni podaci.

import lombok.Getter;
import lombok.Setter;

// iskljuceno : cardNumber i password
@Setter
@Getter
public class ClientDto {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private Integer reservedTraining;
//    private Boolean isActivated; -> treba napraviti logiku za ovo, glupo je da se dodaje u ClientCreateDto
// jer se ne unosi prilikom kreiranja klijenta, vec se menja kasnije

}
