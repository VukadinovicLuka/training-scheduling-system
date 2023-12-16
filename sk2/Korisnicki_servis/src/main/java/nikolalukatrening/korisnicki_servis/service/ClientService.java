package nikolalukatrening.korisnicki_servis.service;

import nikolalukatrening.korisnicki_servis.dto.ClientCreateDto;
import nikolalukatrening.korisnicki_servis.dto.ClientDto;

public interface ClientService {

    ClientDto add(ClientCreateDto clientCreateDto);
}
