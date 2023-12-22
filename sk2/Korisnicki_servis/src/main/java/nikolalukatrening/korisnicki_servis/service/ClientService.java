package nikolalukatrening.korisnicki_servis.service;

import nikolalukatrening.korisnicki_servis.dto.*;
import nikolalukatrening.korisnicki_servis.model.Client;

import java.util.List;

public interface ClientService {

    ClientDto add(ClientCreateDto clientCreateDto);

    ClientDto update (ClientUpdateDto clientUpdateDto);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    ClaimResponseDto getClaim(TokenResponseDto tokenResponseDto);

    List<Client> getAllClients();

    ClientAdminDto updateClientActivation(ClientAdminDto clientAdminDto);

}
