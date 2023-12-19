package nikolalukatrening.korisnicki_servis.service;

import nikolalukatrening.korisnicki_servis.dto.ClientCreateDto;
import nikolalukatrening.korisnicki_servis.dto.ClientDto;
import nikolalukatrening.korisnicki_servis.dto.ClientUpdateDto;

public interface ClientService {

    ClientDto add(ClientCreateDto clientCreateDto);

    ClientDto update (ClientUpdateDto clientUpdateDto);
}
